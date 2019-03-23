import java.util.LinkedHashSet;
import java.util.Set;

public class Employee {

    private String id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String position;
    private Set<String> skills;
    private String managerId;

//    private Employee(EmployeeBuilder builder) {
//        this.id = builder.id;
//        this.firstName = builder.firstName;
//        this.lastName = builder.lastName;
//        this.birthDate = builder.birthDate;
//        this.position = builder.position;
//        this.skills = builder.skills;
//        this.managerId = builder.managerId;
//    }


    public Employee(String id, String firstName, String lastName, String birthDate, String position, Set<String> skills,
                    String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.position = position;
        this.skills = skills;
        this.managerId = managerId;
    }

    public Employee(String id, String firstName, String lastName, String birthDate, String position, String managerId) {
        this(id, firstName, lastName, birthDate, position, new LinkedHashSet<>(), managerId);
    }

    public void addSkill(String skill) {
        this.skills.add(skill);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPosition() {
        return position;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public String getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", position='" + position + '\'' +
                ", skills=" + skills +
                ", managerId='" + managerId + '\'' +
                '}';
    }

//    public static class EmployeeBuilder {
//
//        private String id;
//        private String firstName;
//        private String lastName;
//        private String birthDate;
//        private String position;
//        private Set<String> skills;
//        private String managerId;
//
//        public EmployeeBuilder(Element employee) {
//
//            this.id = employee.getAttributeValue("empId");
//            this.firstName = employee.getChildText("firstName");
//            this.lastName = employee.getChildText("lastName");
//            this.birthDate = employee.getChildText("birthDate");
//            this.position = employee.getChildText("position");
//            this.skills = new HashSet<>();
//            this.managerId = employee.getChildText("managerId");
//
//            Element skillElement = employee.getChild("skills");
//            if (skillElement != null)
//                for (Element skill : skillElement.getChildren())
//                    skills.add(skill.getText());
//        }
//
//        public EmployeeBuilder(String id, String firstName, String lastName, String position, String managerId) {
//            this.id = id;
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.birthDate = null;
//            this.position = position;
//            this.skills = new HashSet<>();
//            this.managerId = managerId;
//        }
//
//        public EmployeeBuilder withSkill(String skill) {
//            skills.add(skill);
//            return this;
//        }
//
//        public Employee build() {
//            return new Employee(this);
//        }
//    }

}
