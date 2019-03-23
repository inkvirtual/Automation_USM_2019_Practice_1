package csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenCSV {

    /**
     * Returns all csv lines, if header is present it will be returned as first line.
     * NOTE: depending on CSV dimensions, it may require a lot of memory!
     *
     * @param fileName csv file to be read from
     * @return csv lines, first line might contain header (if present)
     * @throws IOException
     */
    public static List<String[]> readCSV(String fileName) throws IOException {

        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("Could not read from null/empty filename!");

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

            List<String[]> lines = new ArrayList<>();
            String[] record;

            while ((record = reader.readNext()) != null)
                lines.add(record);

            return lines;

        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Writes the lines to specified file in CSV format.
     *
     * @param fileName file to be written in
     * @param lines    lines, first line might contain the header
     * @throws IOException
     */
    public static void writeToCSV(String fileName, List<String[]> lines) throws IOException {
        writeToCSV(fileName, null, lines);
    }

    /**
     * Writes the header and lines to specified file in CSV format.
     *
     * @param fileName        filename
     * @param separatedHeader header, can be null
     * @param lines           lines without header
     * @throws IOException
     */
    public static void writeToCSV(String fileName, String[] separatedHeader, List<String[]> lines) throws IOException {

        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("Could not write to null/empty filename!");

        if (lines == null)
            throw new IllegalArgumentException("Could not write null lines!");

        if (separatedHeader != null && separatedHeader.length == 0)
            throw new IllegalArgumentException("Header must not be empty!");

        File file = new File(fileName);

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            if (separatedHeader != null)
                writer.writeNext(separatedHeader);

            writer.writeAll(lines);
        } catch (IOException ioe) {
            throw ioe;
        }
    }
}
