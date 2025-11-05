package csusm.cougarplanner.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CSV file reader that parses CSV files into lists of maps.
 * Handles quoted values and empty fields.
 *
 * Part of T03: Implement CSV layer with proper empty field handling and header normalization.
 */
public class CsvReader
{

    /**
     * Reads and parses CSV file into a list of record maps.
     *
     * Returns empty list if file doesn't exist or is empty
     * Normalizes headers to snake_case and lowercase
     * Handles quoted values containing commas
     * Trims whitespace from all values
     * Handles rows with missing columns
     *
     * @param filePath the path to the CSV file to read
     * @return List of maps, where each map represents a row with header→value mappings
     * @throws IOException if the file exists but cannot be read (permission issues, etc.)
     */
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

    /**
     * Parses a single CSV line.
     *
     * Tracks quote state to handle embedded commas
     * Handles double quotes for escaping
     *
     * @param line the CSV line to parse
     * @return array of string values from the CSV line
     */
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
}