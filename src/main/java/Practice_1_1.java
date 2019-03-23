import csv.CSVScannerReader;
import csv.CSVSplitReader;
import csv.CommonCSV;
import csv.OpenCSV;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Practice_1_1 {

    private static final String OPENCSV_FILE = "./Practice_1.1.a_open_csv.csv";
    private static final String COMMONCSV_FILE = "./Practice_1.1.a_common_csv.csv";

    public static void main(String[] args) throws IOException {

        // Generate data to be written to CSV
        List<String[]> students = generateStudents();

        // Practice_1.1.a Write to CSV file using OpenCSV
        OpenCSV.writeToCSV(OPENCSV_FILE, students);

        // Practice_1.1.a Write to CSV file using CommonCSV
        CommonCSV.writeToCSV(COMMONCSV_FILE, students);

        // Practice_1.1.b Read from CSV file using Split
        outputData(CSVSplitReader.readCSV(COMMONCSV_FILE));

        // Practice_1.1.b Read from CSV file using OpenCSV
        outputData(OpenCSV.readCSV(OPENCSV_FILE));

        // Practice_1.1.b Read from CSV file using Scanner
        outputData(CSVScannerReader.readCSV(OPENCSV_FILE));

        // Practice_1.1.b Read from CSV file using common-csv
        outputData(CommonCSV.readCSV(COMMONCSV_FILE));
    }

    private static void outputData(List<String[]> lines) {
        for (String[] line : lines) {
            for (String value : line)
                System.out.printf(String.format("%15s ", value));
            System.out.println();
        }
    }

    private static List<String[]> generateStudents() {
        return Arrays.asList(
                new String[]{"STUDENT", "CLASS", "MARK"},
                new String[]{"Some", "Java", "9"},
                new String[]{"One", "C++", "8"},
                new String[]{"Else", "C", "7"},
                new String[]{"Unknown", "HTML :)))", "10"}
        );
    }
}
