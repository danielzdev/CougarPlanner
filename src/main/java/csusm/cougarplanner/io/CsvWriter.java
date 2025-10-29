package csusm.cougarplanner.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Writes data to CSV files while maintaining data integrity through atomic operations.
 */
public class CsvWriter
{

    /**
     * Writes all records to a CSV file with atomic operation.
     * Uses temporary file and atomic move to prevent data corruption during write.
     *
     * @param filePath the path to the CSV file to write
     * @param records list of maps representing rows, with keys matching headers
     * @param headers column headers for the CSV file
     * @throws IOException if the file cannot be written
     *
     */
    public void writeAll(Path filePath, List<Map<String, String>> records, String[] headers) throws IOException
    {
        CsvPaths.ensureDataDirectory();

        Path tempFile = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        List<String> lines = new ArrayList<>();

        // Writes headers
        lines.add(String.join(",", headers));

        // Writes records
        for (Map<String, String> record : records)
        {
            String[] values = new String[headers.length];
            for (int i = 0; i < headers.length; i++)
            {
                String value = record.get(headers[i]);
                values[i] = escapeCsvValue(value != null ? value : "");
            }
            lines.add(String.join(",", values));
        }

        Files.write(tempFile, lines);
        Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    /**
     * Appends a single record to an existing CSV file.
     * Creates the file with headers if it doesn't exist.
     *
     * @param filePath the path to the CSV file to append to
     * @param record the single record to append as a map
     * @param headers column headers for the CSV file
     * @throws IOException if the file cannot be written or appended to
     */
    public void append(Path filePath, Map<String, String> record, String[] headers) throws IOException
    {
        CsvPaths.ensureDataDirectory();

        // If file doesn't exist, creates it with headers
        if (!Files.exists(filePath))
        {
            writeAll(filePath, List.of(record), headers);
            return;
        }

        // Appends to existing file
        String line = String.join(",",
                java.util.Arrays.stream(headers)
                        .map(header -> escapeCsvValue(record.get(header)))
                        .toArray(String[]::new)
        );

        Files.write(filePath, (System.lineSeparator() + line).getBytes(), java.nio.file.StandardOpenOption.APPEND);
    }

    /**
     * Escapes CSV values according to RFC 4180 CSV formatting rules.
     * Wraps values in quotes if they contain commas, quotes, or newlines.
     *
     * @param value the string value to escape for CSV
     * @return properly escaped CSV value
     */
    private String escapeCsvValue(String value)
    {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n"))
        {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}