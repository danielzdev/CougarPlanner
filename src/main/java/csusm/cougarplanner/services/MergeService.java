package csusm.cougarplanner.services;

import csusm.cougarplanner.models.Announcement;
import csusm.cougarplanner.models.Assignment;
import csusm.cougarplanner.models.Course;
import csusm.cougarplanner.io.AnnouncementsRepository;
import csusm.cougarplanner.io.AssignmentsRepository;
import csusm.cougarplanner.io.CoursesRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for merging fetched data with local CSV files.
 * Updates existing items and adds new ones while preserving local data.
 * For assignments, preserves local difficulty values during merge operations.
 */
public class MergeService {

    private final CoursesRepository coursesRepository;
    private final AssignmentsRepository assignmentsRepository;
    private final AnnouncementsRepository announcementsRepository;

    /**
     * Constructs a new MergeService with repository dependencies.
     */
    public MergeService() {
        this.coursesRepository = new CoursesRepository();
        this.assignmentsRepository = new AssignmentsRepository();
        this.announcementsRepository = new AnnouncementsRepository();
    }

    /**
     * Merges fetched courses with local CSV data.
     * Updates existing courses by ID and adds new ones.
     *
     * @param fetchedCourses List of courses fetched from external API
     * @throws IOException if the CSV file cannot be read or written
     */
    public void mergeCourses(List<Course> fetchedCourses) throws IOException {
        if (fetchedCourses == null || fetchedCourses.isEmpty()) {
            return;
        }

        // Load existing courses into a map for efficient lookup
        Map<String, Course> existingCoursesMap = new HashMap<>();
        List<Course> existingCourses = coursesRepository.findAll();
        for (Course course : existingCourses) {
            existingCoursesMap.put(course.getCourseId(), course);
        }

        // Process each fetched course
        for (Course fetchedCourse : fetchedCourses) {
            if (fetchedCourse.getCourseId() == null) {
                continue; // Skip courses without valid IDs
            }

            // Update existing or add new course
            existingCoursesMap.put(fetchedCourse.getCourseId(), fetchedCourse);
        }

        // Write all courses back to CSV
        coursesRepository.upsertAll(existingCoursesMap.values().stream().toList());
    }

    /**
     * Merges fetched assignments with local CSV data.
     * Updates existing assignments by ID and adds new ones.
     * Preserves local difficulty values when updating existing assignments.
     *
     * @param fetchedAssignments List of assignments fetched from external API
     * @throws IOException if the CSV file cannot be read or written
     */
    public void mergeAssignments(List<Assignment> fetchedAssignments) throws IOException {
        if (fetchedAssignments == null || fetchedAssignments.isEmpty()) {
            return;
        }

        // Load existing assignments into a map for efficient lookup
        Map<String, Assignment> existingAssignmentsMap = new HashMap<>();
        List<Assignment> existingAssignments = assignmentsRepository.findAll();
        for (Assignment assignment : existingAssignments) {
            existingAssignmentsMap.put(assignment.getAssignmentId(), assignment);
        }

        // Process each fetched assignment
        for (Assignment fetchedAssignment : fetchedAssignments) {
            if (fetchedAssignment.getAssignmentId() == null) {
                continue; // Skip assignments without valid IDs
            }

            // Check if assignment exists locally
            Assignment existingAssignment = existingAssignmentsMap.get(fetchedAssignment.getAssignmentId());

            if (existingAssignment != null) {
                // Preserve local difficulty value when updating existing assignment
                Integer localDifficulty = existingAssignment.getDifficulty();
                fetchedAssignment.setDifficulty(localDifficulty);
            }
            // If assignment is new, difficulty will remain null (as set by CanvasService)

            // Update or add assignment
            existingAssignmentsMap.put(fetchedAssignment.getAssignmentId(), fetchedAssignment);
        }

        // Write all assignments back to CSV
        assignmentsRepository.upsertAll(existingAssignmentsMap.values().stream().toList());
    }

    /**
     * Merges fetched announcements with local CSV data.
     * Updates existing announcements by ID and adds new ones.
     *
     * @param fetchedAnnouncements List of announcements fetched from external API
     * @throws IOException if the CSV file cannot be read or written
     */
    public void mergeAnnouncements(List<Announcement> fetchedAnnouncements) throws IOException {
        if (fetchedAnnouncements == null || fetchedAnnouncements.isEmpty()) {
            return;
        }

        // Load existing announcements into a map for efficient lookup
        Map<String, Announcement> existingAnnouncementsMap = new HashMap<>();
        List<Announcement> existingAnnouncements = announcementsRepository.findAll();
        for (Announcement announcement : existingAnnouncements) {
            existingAnnouncementsMap.put(announcement.getAnnouncementId(), announcement);
        }

        // Process each fetched announcement
        for (Announcement fetchedAnnouncement : fetchedAnnouncements) {
            if (fetchedAnnouncement.getAnnouncementId() == null) {
                continue; // Skip announcements without valid IDs
            }

            // Update existing or add new announcement
            existingAnnouncementsMap.put(fetchedAnnouncement.getAnnouncementId(), fetchedAnnouncement);
        }

        // Write all announcements back to CSV
        announcementsRepository.upsertAll(existingAnnouncementsMap.values().stream().toList());
    }

    /**
     * Performs a complete merge operation for all data types.
     * Convenience method that merges courses, assignments, and announcements.
     *
     * @param fetchedCourses List of courses to merge
     * @param fetchedAssignments List of assignments to merge
     * @param fetchedAnnouncements List of announcements to merge
     * @throws IOException if any CSV file cannot be read or written
     */
    public void mergeAll(
        List<Course> fetchedCourses,
        List<Assignment> fetchedAssignments,
        List<Announcement> fetchedAnnouncements
    ) throws IOException {
        mergeCourses(fetchedCourses);
        mergeAssignments(fetchedAssignments);
        mergeAnnouncements(fetchedAnnouncements);
    }
}
