package csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVScannerReader {

    /**
     * Returns all csv lines, if header is present it will be returned as first line.
     * NOTE: depending on CSV dimensions, it may require a lot of memory!
     *
     * @param fileName csv file to be read from
     * @return csv lines, first line might contain header (if present)
     * @throws FileNotFoundException
     */
    public static List<String[]> readCSV(String fileName) throws FileNotFoundException {
        return readCSV(fileName, ",");
    }

    /**
     * Returns all csv lines, if header is present it will be returned as first line.
     * NOTE: depending on CSV dimensions, it may require a lot of memory!
     *
     * @param fileName  csv file to be read from
     * @param separator separator
     * @return csv lines, first line might contain header (if present)
     * @throws FileNotFoundException
     */
    public static List<String[]> readCSV(String fileName, String separator) throws FileNotFoundException {

        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("Could not read from null/empty filename!");

        if (separator == null || separator.length() == 0)
            throw new IllegalArgumentException("Could not read CSV file using null/empty separator!");

        Scanner scanner = new Scanner(new File(fileName));

        List<String[]> lines = new ArrayList<>();

        while (scanner.hasNext())
            lines.add(scanner.nextLine().split(separator));

        return lines;
    }
}
