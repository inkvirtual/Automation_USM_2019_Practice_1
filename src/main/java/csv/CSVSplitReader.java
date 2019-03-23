package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSplitReader {

    /**
     * Returns all csv lines, if header is present it will be returned as first line.
     * NOTE: depending on CSV dimensions, it may require a lot of memory!
     *
     * @param fileName csv file to be read from
     * @return csv lines, first line might contain header (if present)
     * @throws IOException
     */
    public static List<String[]> readCSV(String fileName) throws IOException {
        return readCSV(fileName, ",");
    }

    /**
     * Returns all csv lines, if header is present it will be returned as first line.
     * NOTE: depending on CSV dimensions, it may require a lot of memory!
     *
     * @param fileName  csv file to be read from
     * @param separator separator
     * @return csv lines, first line might contain header (if present)
     * @throws IOException
     */
    public static List<String[]> readCSV(String fileName, String separator) throws IOException {

        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("Could not read from null/empty filename!");

        if (separator == null || separator.length() == 0)
            throw new IllegalArgumentException("Could not read CSV file using null/empty separator!");

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            List<String[]> lines = new ArrayList<>();

            while ((line = reader.readLine()) != null)
                lines.add(line.split(separator));

            return lines;
        } catch (IOException e) {
            throw e;
        }
    }
}
