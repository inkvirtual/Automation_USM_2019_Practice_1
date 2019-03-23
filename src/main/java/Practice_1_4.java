import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Practice_1_4 {

    private static final String OUTPUT_XLSX = "./Practice_1.4.xlsx";

    public static void main(String[] args) throws IOException, ParseException {

        // Convert JSON to Workbook (XLSX)
        Workbook workbookXLSX = jsonToXLSX(Practice_1_3.getJsonContent(Practice_1_3.INPUT_JSON));

        // Write Workbook to excel file
        writeExcelToFile(workbookXLSX, OUTPUT_XLSX);
    }

    private static Workbook jsonToXLSX(String jsonContent) throws ParseException {

        JSONParser parser = new JSONParser();

        Workbook workbook = new XSSFWorkbook();

        JSONObject root = (JSONObject) parser.parse(jsonContent);
        JSONArray departments = (JSONArray) root.get("company");

        for (int i = 0; i < departments.size(); i++) {

            JSONObject department = (JSONObject) departments.get(i);
            String departmentName = (String) department.get("department");
            String departmentId = (String) department.get("depId");

            Sheet departmentSheet = workbook.createSheet(departmentName + "-" + departmentId);

            Row employeeHeaderRow = departmentSheet.createRow(0);
            employeeHeaderRow.setHeightInPoints(25);

            CellStyle employeeHeaderCellStyle = workbook.createCellStyle();
            employeeHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            employeeHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            employeeHeaderCellStyle.setBorderBottom(BorderStyle.THICK);
            employeeHeaderCellStyle.setBorderLeft(BorderStyle.THICK);
            employeeHeaderCellStyle.setBorderRight(BorderStyle.THICK);
            employeeHeaderCellStyle.setBorderTop(BorderStyle.THICK);

            Font employeeHeaderCellFont = workbook.createFont();
            employeeHeaderCellFont.setBold(true);

            employeeHeaderCellStyle.setFont(employeeHeaderCellFont);

            createCell(employeeHeaderRow, 1, "Emp ID", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 2, "Last Name", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 3, "First Name", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 4, "Birth date", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 5, "Manager ID", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 6, "Position", employeeHeaderCellStyle);
            createCell(employeeHeaderRow, 7, "Skills", employeeHeaderCellStyle);

            JSONArray employees = (JSONArray) department.get("employees");

            for (int j = 0; j < employees.size(); j++) {

                JSONObject employee = (JSONObject) employees.get(j);

                String empId = (String) employee.get("empId");
                String lastName = (String) employee.get("lastName");
                String firstName = (String) employee.get("firstName");
                String birthDate = (String) employee.get("birthDate");
                String position = (String) employee.get("position");
                String managerId = (String) employee.get("managerId");

                List<String> skillsList = new LinkedList<>();
                JSONArray skills = (JSONArray) employee.get("skills");

                for (int k = 0; k < skills.size(); k++) {
                    String skill = (String) ((JSONObject) skills.get(k)).get("skill");
                    skillsList.add(skill);
                }

                Row employeeRow = departmentSheet.createRow(j + 1);
                CellStyle employeeCellStyle = workbook.createCellStyle();

                createCell(employeeRow, 1, empId, employeeCellStyle);
                createCell(employeeRow, 2, lastName, employeeCellStyle);
                createCell(employeeRow, 3, firstName, employeeCellStyle);
                createCell(employeeRow, 4, birthDate, employeeCellStyle);
                createCell(employeeRow, 5, managerId, employeeCellStyle);
                createCell(employeeRow, 6, position, employeeCellStyle);

                CellStyle skillsCellStyle = workbook.createCellStyle();
                skillsCellStyle.setWrapText(true);
                skillsCellStyle.setShrinkToFit(true);

                employeeRow.setHeightInPoints((10 * departmentSheet.getDefaultRowHeightInPoints()));

                StringBuilder skillsListValue = new StringBuilder();
                for (String skillValue : skillsList)
                    skillsListValue.append(skillValue).append("\n");

                createCell(employeeRow, 7, skillsListValue.toString(), skillsCellStyle);
            }

            departmentSheet.autoSizeColumn(1);
            departmentSheet.autoSizeColumn(2);
            departmentSheet.autoSizeColumn(3);
            departmentSheet.autoSizeColumn(4);
            departmentSheet.autoSizeColumn(5);
            departmentSheet.autoSizeColumn(6);
        }


        return workbook;
    }

    private static void createCell(Row row, int column, String value, CellStyle cellStyle) {

        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private static void writeExcelToFile(Workbook excel, String fileName) throws IOException {

        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            excel.write(outputStream);
        }
    }
}
