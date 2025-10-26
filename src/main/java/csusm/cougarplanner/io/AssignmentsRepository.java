package csusm.cougarplanner.io;

import csusm.cougarplanner.Assignment;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import csusm.cougarplanner.util.DateTimeUtil;
import csusm.cougarplanner.util.WeekUtil;

public class AssignmentsRepository
{
    private static final String[] HEADERS = {"assignment_id", "course_id", "assignment_name", "due_date", "due_time", "difficulty"};
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    public AssignmentsRepository()
    {
        this.csvReader = new CsvReader();
        this.csvWriter = new CsvWriter();
    }

    public List<Assignment> findAll() throws IOException
    {
        List<Map<String, String>> records = csvReader.readAll(CsvPaths.getAssignmentsPath());
        return records.stream()
                .map(this::mapToAssignment)
                .collect(Collectors.toList());
    }

    public List<Assignment> findByWeek(LocalDate weekStart, LocalDate weekEnd) throws IOException
    {
        return findAll().stream()
                .filter(assignment -> isInWeek(assignment, weekStart, weekEnd))
                .collect(Collectors.toList());
    }

    public List<Assignment> findByDay(LocalDate day) throws IOException
    {
        return findAll().stream()
                .filter(assignment -> isOnDay(assignment, day))
                .collect(Collectors.toList());
    }

    public void upsert(Assignment assignment) throws IOException
    {
        List<Assignment> allAssignments = findAll();

        // Removes existing assignment with same ID
        allAssignments.removeIf(a -> a.getAssignmentId().equals(assignment.getAssignmentId()));

        // Adds updated assignment (preserving difficulty if exists)
        allAssignments.add(assignment);

        // Writes back to file
        List<Map<String, String>> records = allAssignments.stream()
                .map(this::assignmentToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getAssignmentsPath(), records, HEADERS);
    }

    public void upsertAll(List<Assignment> assignments) throws IOException
    {
        Map<String, Assignment> assignmentMap = new HashMap<>();

        // Loads existing assignments
        for (Assignment existing : findAll())
        {
            assignmentMap.put(existing.getAssignmentId(), existing);
        }

        // Updates with new assignments
        for (Assignment assignment : assignments)
        {
            Assignment existing = assignmentMap.get(assignment.getAssignmentId());
            if (existing != null && existing.getDifficulty() != null)
            {
                assignment.setDifficulty(existing.getDifficulty());
            }
            assignmentMap.put(assignment.getAssignmentId(), assignment);
        }

        // Writes back
        List<Map<String, String>> records = assignmentMap.values().stream()
                .map(this::assignmentToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getAssignmentsPath(), records, HEADERS);
    }

    private boolean isInWeek(Assignment assignment, LocalDate weekStart, LocalDate weekEnd)
    {
        LocalDate dueDate = DateTimeUtil.parseDate(assignment.getDueDate());
        return WeekUtil.isDateInWeek(dueDate, weekStart, weekEnd);
    }

    private boolean isOnDay(Assignment assignment, LocalDate day)
    {
        LocalDate dueDate = DateTimeUtil.parseDate(assignment.getDueDate());
        return dueDate != null && dueDate.equals(day);
    }

    private Assignment mapToAssignment(Map<String, String> record)
    {
        Assignment assignment = new Assignment();
        assignment.setAssignmentId(record.get("assignment_id"));
        assignment.setCourseId(record.get("course_id"));
        assignment.setAssignmentName(record.get("assignment_name"));
        assignment.setDueDate(record.get("due_date"));
        assignment.setDueTime(record.get("due_time"));

        String difficultyStr = record.get("difficulty");
        if (difficultyStr != null && !difficultyStr.isEmpty())
        {
            try
            {
                assignment.setDifficulty(Integer.parseInt(difficultyStr));
            }
            catch (NumberFormatException e)
            {
                assignment.setDifficulty(null);
            }
        }
        return assignment;
    }

    private Map<String, String> assignmentToMap(Assignment assignment)
    {
        Map<String, String> record = new HashMap<>();
        record.put("assignment_id", assignment.getAssignmentId());
        record.put("course_id", assignment.getCourseId());
        record.put("assignment_name", assignment.getAssignmentName());
        record.put("due_date", assignment.getDueDate());
        record.put("due_time", assignment.getDueTime());
        record.put("difficulty", assignment.getDifficulty() != null ?
                assignment.getDifficulty().toString() : "");
        return record;
    }
}