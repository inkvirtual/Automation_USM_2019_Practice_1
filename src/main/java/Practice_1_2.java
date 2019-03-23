import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

public class Practice_1_2 {

    private static final String INPUT_XML = "./Practice_1.2.a.xml";
    private static final String OUTPUT_XML = "./Practice_1.2.b.xml";

    public static void main(String[] args) throws Exception {

        // Practice_1.2.b Parse XML file
        parseXML(INPUT_XML);

        // Practice_1.2.b Query XML file by empId
        {
            String empId = "003";
            Employee employee = queryXmlByEmployeeId(INPUT_XML, empId).
                    orElseThrow(() -> new NoSuchElementException(String.format("Employee '%s' has not been found", empId)));
            System.out.println(employee);
        }

        // Practice_1.2.b Create a department with one employee
        {
            Element departmentElement = createDepartmentElement("DevOps", "003");
            Employee devOpsMan = new Employee("007", "DevOpsManFN", "DevOpsManLN", "01.01.2000", "Department Manager", "000");
            devOpsMan.addSkill("See previous managers");
            devOpsMan.addSkill("Does nothing, but who cares as long as client is paying...");
            Employee devOpsEmp = new Employee("008", "DevOpsFN", "DevOpsLN", "01.01.2000", "DevOps", "007");
            devOpsEmp.addSkill("Nobody knows what he does, but who cares as long as client is paying...");
            departmentElement.addContent(createEmployeeElement(devOpsMan));
            departmentElement.addContent(createEmployeeElement(devOpsEmp));

            System.out.println(String.format("Adding to '%s' the '%s' department and store result to '%s'...",
                    INPUT_XML, departmentElement.getAttributeValue("name"), OUTPUT_XML));
            Element root = getDocument(INPUT_XML).getRootElement();
            root.addContent(departmentElement);

            // Add department to XML
            writeXML(root.getDocument(), OUTPUT_XML);
        }

        // Practice_1.2.b.i Query XML and output all Employees
        System.out.println(queryEmployeesFromXML(INPUT_XML));

        // Practice_1.2.c Query by empId using StAX
        {
            final String empId = "003";

            // Query by empId using StAX
            System.out.println(queryXML(INPUT_XML, empId).orElseThrow(() -> new NoSuchElementException("Did not find employee " + empId)));
        }

        // Practice_1.2.d using XPATH
        {
            String tag = "skills";

            // Check tag presence
            boolean tagIsPresent = checkTagPresence(INPUT_XML, tag);
            System.out.printf("Tag '%s' %s present\n", tag, tagIsPresent ? "is" : "is not");

            // Check if tag contains children
            boolean tagHasChildren = checkTagHasChildren(INPUT_XML, tag);
            System.out.printf("Tag '%s' %s children\n", tag, tagHasChildren ? "has" : "has not");

            // Return list of values for specified tag
            tag = "skill";
            List<String> tagValues = getTagValues(Practice_1_2.INPUT_XML, tag);
            System.out.printf("Tag '%s' has following values: \n", tag);
            tagValues.forEach((tagValue) -> System.out.println("\t" + tagValue));
        }
    }

    private static void writeXML(Document document, String fileName) throws IOException {

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, new FileWriter(new File(fileName)));
    }

    private static Element createDepartmentElement(String name, String depId) {

        Element department = new Element("department");
        department.setAttribute(new Attribute("name", name));
        department.setAttribute(new Attribute("depId", depId));

        return department;
    }

    private static Element createEmployeeElement(Employee employee) {

        Element employeeElement = new Element("employee");

        employeeElement.setAttribute(new Attribute("empId", employee.getId()));

        Element firstName = new Element("firstName");
        firstName.setText(employee.getFirstName());
        employeeElement.addContent(firstName);

        Element lastName = new Element("lastName");
        lastName.setText(employee.getLastName());
        employeeElement.addContent(lastName);

        Element birthDate = new Element("birthDate");
        birthDate.setText(employee.getBirthDate());
        employeeElement.addContent(birthDate);

        Element position = new Element("position");
        position.setText(employee.getPosition());
        employeeElement.addContent(position);

        Element skillsElement = new Element("skills");
        for (String skillAsString : employee.getSkills()) {
            Element skillElement = new Element("skill");
            skillElement.setText(skillAsString);
            skillsElement.addContent(skillElement);
        }
        employeeElement.addContent(skillsElement);

        Element managerId = new Element("managerId");
        managerId.setText(employee.getManagerId());
        employeeElement.addContent(managerId);

        return employeeElement;
    }

    public static Optional<Employee> queryXmlByEmployeeId(String fileName, String empId) throws JDOMException, IOException {

        System.out.println(String.format("Query '%s' by 'empId=%s'", fileName, empId));

        for (Element department : getDocument(fileName).getRootElement().getChildren())
            for (Element employee : department.getChildren())
                if (empId.equals(employee.getAttributeValue("empId")))
                    return Optional.ofNullable(extractEmployee(employee));

        System.out.println(String.format("No employee with 'empId=%s' has been found.", empId));
        return Optional.empty();
    }

    public static Employee extractEmployee(Element empElement) {

        Employee employee = new Employee(
                empElement.getAttributeValue("empId"),
                empElement.getChildText("firstName"),
                empElement.getChildText("lastName"),
                empElement.getChildText("birthDate"),
                empElement.getChildText("position"),
                empElement.getChildText("managerId"));

        Element skillElement = empElement.getChild("skills");

        if (skillElement != null)
            for (Element skill : skillElement.getChildren())
                employee.addSkill(skill.getText());

        return employee;
    }

    public static Document getDocument(String fileName) throws JDOMException, IOException {
        File inputFile = new File(fileName);
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(inputFile);
    }

    private static void parseXML(String fileName) throws IOException, JDOMException {

        System.out.println(String.format("Parsing XML '%s'...", fileName));
        Element root = getDocument(fileName).getRootElement();
        System.out.println("\t\t" + root.getName());

        System.out.println("----------------------");

        for (Element department : root.getChildren()) {

            System.out.println("\tDepartment: " + department.getAttributeValue("name"));
            System.out.println("Employees: ");

            for (Element employee : department.getChildren())
                outputEmployeeDetails(employee);

            System.out.println("- - - - - - - - -");
        }

        System.out.println("Parse finished!");
    }

    private static void outputEmployeeDetails(Element employee) {

        System.out.println(employee.getChildText("firstName"));
        System.out.println(employee.getChildText("lastName"));
        System.out.println(employee.getChildText("birthDate"));
        System.out.println(employee.getChildText("position"));

        System.out.println("Skills:");
        for (Element skill : employee.getChild("skills").getChildren()) {
            System.out.println("- " + skill.getText());
        }
        System.out.println("-  -  -  -  -  -");
    }

    private static List<Employee> queryEmployeesFromXML(String fileName) throws JDOMException, IOException {

        System.out.println(String.format("Query '%s' to extract employees", fileName));

        List<Employee> employees = new LinkedList<>();

        // Extract all employees (managers, managers of managers and prostie smertnie)
        for (Element department : getDocument(fileName).getRootElement().getChildren())
            for (Element employee : department.getChildren())
                employees.add(extractEmployee(employee));

        return employees;
    }

    private static Optional<Employee> queryXML(String fileName, String empId) throws FileNotFoundException, XMLStreamException {

        boolean bFirstName = false;
        boolean bLastName = false;
        boolean bBirthDate = false;
        boolean bPosition = false;
        boolean bSkills = false;
        boolean bManagerId = false;
        boolean isRequestedEmpId = false;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new FileReader(fileName));

        String firstName = "NA";
        String lastName = "NA";
        String birthDate = "NA";
        String position = "NA";
        Set<String> skills = new LinkedHashSet<>();
        String managerId = "NA";

        String actualEmpId = null;

        while (reader.hasNext()) {

            XMLEvent event = reader.nextEvent();

            switch (event.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("employee")) {
                        Iterator<javax.xml.stream.events.Attribute> attribs = startElement.getAttributes();

                        if (attribs.hasNext())
                            actualEmpId = attribs.next().getValue();

                        if (empId.equalsIgnoreCase(actualEmpId))
                            isRequestedEmpId = true;
                        else
                            continue;
                    } else if (!empId.equalsIgnoreCase(actualEmpId))
                        continue;
                    else if (qName.equalsIgnoreCase("firstName"))
                        bFirstName = true;
                    else if (qName.equalsIgnoreCase("lastName"))
                        bLastName = true;
                    else if (qName.equalsIgnoreCase("birthDate"))
                        bBirthDate = true;
                    else if (qName.equalsIgnoreCase("position"))
                        bPosition = true;
                    else if (qName.equalsIgnoreCase("skill"))
                        bSkills = true;
                    else if (qName.equalsIgnoreCase("managerId"))
                        bManagerId = true;
                    break;

                case XMLStreamConstants.CHARACTERS:
                    Characters chars = event.asCharacters();

                    if (isRequestedEmpId) {

                        if (bFirstName) {
                            firstName = chars.getData();
                            bFirstName = false;
                        } else if (bLastName) {
                            lastName = chars.getData();
                            bLastName = false;
                        } else if (bBirthDate) {
                            birthDate = chars.getData();
                            bBirthDate = false;
                        } else if (bPosition) {
                            position = chars.getData();
                            bPosition = false;
                        } else if (bSkills) {
                            skills.add(chars.getData());
                            bSkills = false;
                        } else if (bManagerId) {
                            managerId = chars.getData();
                            bManagerId = false;
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    switch (endElement.getName().getLocalPart()) {
                        case "employee":
                            if (isRequestedEmpId) {
                                isRequestedEmpId = false;

                                return Optional.of(new Employee(empId, firstName, lastName, birthDate, position, skills, managerId));
                            }
                    }
                    break;

            }
        }

        return Optional.empty();
    }

    private static List<String> getTagValues(String xml, String tag) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = dBuilder.parse(new File(xml));
        doc.getDocumentElement().normalize();

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile("//" + tag).evaluate(doc, XPathConstants.NODESET);

        List<String> tagValues = new LinkedList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                tagValues.add(((org.w3c.dom.Element) node).getTextContent());
        }

        return tagValues;
    }

    private static boolean checkTagHasChildren(String xml, String tag) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = dBuilder.parse(new File(xml));

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList node = (NodeList) xPath.compile("//" + tag).evaluate(doc, XPathConstants.NODESET);

        if (node == null || node.getLength() == 0)
            return false;

        return node.item(0).getChildNodes().getLength() > 1;
    }

    private static boolean checkTagPresence(String xml, String tag) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xml));
        doc.getDocumentElement().normalize();


        XPath xPath = XPathFactory.newInstance().newXPath();
        return ((NodeList) xPath.compile("//" + tag).evaluate(doc, XPathConstants.NODESET)).getLength() > 0;
    }
}
