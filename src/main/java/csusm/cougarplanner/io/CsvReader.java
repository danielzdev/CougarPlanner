package csusm.cougarplanner.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CsvReader
{

    public List<Map<String, String>> readAll(Path filePath) throws IOException
    {
        if (!Files.exists(filePath))
        {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty())
        {
            return new ArrayList<>();
        }

        // Parses headers
        String[] headers = Arrays.stream(lines.get(0).split(","))
                .map(String::trim)
                .map(this::toSnakeCase)
                .map(String::toLowerCase)
                .toArray(String[]::new);

        // Parses data rows
        List<Map<String, String>> records = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++)
        {
            String[] values = parseCsvLine(lines.get(i));
            Map<String, String> record = new HashMap<>();

            for (int j = 0; j < Math.min(headers.length, values.length); j++)
            {
                record.put(headers[j], values[j].trim());
            }
            records.add(record);
        }

        return records;
    }

    private String[] parseCsvLine(String line)
    {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray())
        {
            if (c == '"')
            {
                inQuotes = !inQuotes;
            }
            else if (c == ',' && !inQuotes)
            {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            }
            else
            {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }

    private String toSnakeCase(String header)
    {
        return header.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}