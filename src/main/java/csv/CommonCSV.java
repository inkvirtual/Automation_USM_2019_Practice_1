package csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommonCSV {

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

        try (Reader reader = Files.newBufferedReader(Paths.get(fileName));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            List<String[]> lines = new ArrayList<>();

            int recordLength;
            for (CSVRecord csvRecord : csvParser) {

                recordLength = csvRecord.size();
                String[] record = new String[recordLength];

                for (int i = 0; i < recordLength; i++)
                    record[i] = csvRecord.get(i);

                lines.add(record);
            }

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
     * @param separatedHeader header
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

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            if (separatedHeader != null)
                csvPrinter.printRecord(separatedHeader);

            for (String[] line : lines)
                csvPrinter.printRecord(line);

            csvPrinter.flush();
        }

    }
}
