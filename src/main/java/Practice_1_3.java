
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Practice_1_3 {

    public static final String INPUT_JSON = "./Practice_1.3.a.json";
    private static final String OUTPUT_JSON = "./Practice_1.3.c.json";

    public static void main(String[] args) throws IOException, ParseException {

        String jsonContent = getJsonContent(INPUT_JSON);

        // Practice_1.3.b Parse JSON
        parseJSON(jsonContent);

        // Practice_1.3.c create part of JSON (1 dep with 1 emp)
        {
            // Get JSON object
            JSONObject jsonObject = getJsonObject(jsonContent);

            // Create DevOps department with one manager
            JSONObject devOpsDep = createDevOpsDepartment();

            // Add DevOps department
            addDepartmentToJSON(jsonObject, devOpsDep);

            // Write JSON to file
            writeJsonToFile(jsonObject, OUTPUT_JSON);
        }
    }

    private static JSONObject createDevOpsDepartment() {

        JSONObject department = new JSONObject();
        department.put("department", "DevOps");
        department.put("depId", "003");

        JSONArray employees = new JSONArray();
        JSONObject employeeSubObj = new JSONObject();
        employeeSubObj.put("empId", "007");
        employeeSubObj.put("firstName", "DevOpsManFN");
        employeeSubObj.put("lastName", "DevOpsManLN");
        employeeSubObj.put("birthDate", "01.01.2000");
        employeeSubObj.put("position", "Department Manager");
        employeeSubObj.put("managerId", "000");

        JSONArray skillsArray = new JSONArray();
        JSONObject skill = new JSONObject();
        skill.put("skill", "Does nothing, but who cares as long as client is paying...");
        skillsArray.add(skill);
        employeeSubObj.put("skills", skillsArray);
        employees.add(employeeSubObj);
        department.put("employees", employees);

        return department;
    }

    private static JSONObject createJsonObject(Map<Object, Object> map) {
        return new JSONObject(map);
    }

    private static void addDepartmentToJSON(JSONObject json, JSONObject department) throws ParseException {

        JSONArray departments = (JSONArray) json.get("company");
        departments.add(department);
    }

    private static JSONObject getJsonObject(String json) throws ParseException {

        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(json);
    }

    private static void writeJsonToFile(JSONObject json, String outputFile) throws IOException {

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(json.toJSONString());
            writer.flush();
        }
//        catch (Exception e) {
//            throw e;
//        }
    }

    private static void parseJSON(String jsonContent) throws ParseException {

        JSONParser parser = new JSONParser();

        JSONObject root = (JSONObject) parser.parse(jsonContent);
        JSONArray departments = (JSONArray) root.get("company");

        for (int i = 0; i < departments.size(); i++) {

            JSONObject department = (JSONObject) departments.get(i);
            System.out.println("Department: " + department.get("department"));
            System.out.println("depId: " + department.get("depId"));

            JSONArray employees = (JSONArray) department.get("employees");
            System.out.println("Employees: ");

            for (int j = 0; j < employees.size(); j++) {

                JSONObject employee = (JSONObject) employees.get(j);
                System.out.println("\tempId: " + employee.get("empId"));
                System.out.println("\tlastName: " + employee.get("lastName"));
                System.out.println("\tfirstName: " + employee.get("firstName"));
                System.out.println("\tbirthDate: " + employee.get("birthDate"));
                System.out.println("\tposition: " + employee.get("position"));
                System.out.println("\tmanagerId: " + employee.get("managerId"));

                JSONArray skills = (JSONArray) employee.get("skills");
                System.out.println("\tSkills:");

                for (int k = 0; k < skills.size(); k++)
                    System.out.println("\t\t" + ((JSONObject) skills.get(k)).get("skill"));

                System.out.println();
            }
        }
    }

    public static String getJsonContent(String fileName) throws IOException {

        final List<String> fileLines = Files.readAllLines(Paths.get(fileName));
        final StringBuilder content = new StringBuilder();
        fileLines.forEach(line -> content.append(line).append(System.lineSeparator()));

        return content.toString();
    }
}
