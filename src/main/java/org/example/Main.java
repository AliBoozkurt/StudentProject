package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static int schoolIdCounter = 1;
    private static int studentIdCounter = 1;

    public static void main(String[] args) {
        List<School> schools = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        File excelFile = new File("SchoolData.xlsx");
        if (excelFile.exists()) {
            System.out.println("The Excel file 'SchoolData.xlsx' already exists. Do you want to load existing data? (yes/no)");
            String loadChoice = scanner.nextLine().trim().toLowerCase();
            if (loadChoice.equals("yes")) {
                schools = ExcelUtil.loadSchoolDataFromExcel("SchoolData.xlsx", "StudentData.xlsx");
                // Update counters based on loaded data
                updateCounters(schools);
            }
        }

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add school");
            System.out.println("2. Add student");
            System.out.println("3. Edit school");
            System.out.println("4. Edit student");
            System.out.println("5. List all schools");
            System.out.println("6. List by school");
            System.out.println("7. Search student");
            System.out.println("8. Export to CSV");
            System.out.println("9. Export student data");
            System.out.println("10. Export student data to separate file");
            System.out.println("11. Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addSchool(scanner, schools);
                    break;
                case "2":
                    addStudent(scanner, schools);
                    break;
                case "3":
                    editSchool(scanner, schools);
                    break;
                case "4":
                    editStudent(scanner, schools);
                    break;
                case "5":
                    listAllSchools(schools);
                    break;
                case "6":
                    listBySchool(scanner, schools);
                    break;
                case "7":
                    searchStudent(scanner, schools);
                    break;
                case "8":
                    exportDataToCSV(schools);
                    break;
                case "9":
                    exportStudentDataToExcel(schools);
                    break;
                case "10":
                    exportStudentDataToSeparateExcel(schools);
                    break;
                case "11":
                    ExcelUtil.saveSchoolDataToExcel("SchoolData.xlsx", schools);
                    ExcelUtil.saveStudentDataToSeparateExcel("StudentData.xlsx", schools);
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 11.");
            }
        }
    }

    private static void updateCounters(List<School> schools) {
        for (School school : schools) {
            if (school.getId() >= schoolIdCounter) {
                schoolIdCounter = school.getId() + 1;
            }
            for (Student student : school.getStudents()) {
                if (student.getId() >= studentIdCounter) {
                    studentIdCounter = student.getId() + 1;
                }
            }
        }
    }

    private static void addSchool(Scanner scanner, List<School> schools) {
        System.out.println("Enter the name of the school:");
        String name = scanner.nextLine();
        System.out.println("Enter the school code:");
        String schoolCode = scanner.nextLine();
        System.out.println("Enter the address of the school:");
        String address = scanner.nextLine();

        School school = new School(schoolIdCounter++, name, schoolCode, address);
        schools.add(school);
    }

    private static void addStudent(Scanner scanner, List<School> schools) {
        if (schools.isEmpty()) {
            System.out.println("No schools available. Please add a school first.");
            return;
        }

        System.out.println("Enter the name or code of the school to add the student to:");
        String schoolIdentifier = scanner.nextLine();
        School school = findSchoolByNameOrCode(schools, schoolIdentifier);

        if (school == null) {
            System.out.println("School not found. Available schools are:");
            for (School s : schools) {
                System.out.println("- " + s.getName() + " (Code: " + s.getSchoolCode() + ")");
            }
            return;
        }

        System.out.println("Enter the name of the student:");
        String studentName = scanner.nextLine();
        System.out.println("Enter the age of the student:");
        int age = getValidIntegerInput(scanner);
        String studentId = "10";

        Student student = new Student(studentIdCounter++, studentName, age, studentId, school.getId());
        school.addStudent(student);
    }

    private static void editSchool(Scanner scanner, List<School> schools) {
        System.out.println("Enter the school code of the school to edit:");
        String schoolCode = scanner.nextLine();
        School school = findSchoolByCode(schools, schoolCode);

        if (school == null) {
            System.out.println("School not found. Available schools are:");
            for (School s : schools) {
                System.out.println("- " + s.getName() + " (Code: " + s.getSchoolCode() + ")");
            }
            return;
        }

        System.out.println("Enter the new name of the school:");
        school.setName(scanner.nextLine());
        System.out.println("Enter the new address of the school:");
        school.setAddress(scanner.nextLine());
    }

    private static void editStudent(Scanner scanner, List<School> schools) {
        System.out.println("Enter the student ID of the student to edit:");
        String studentId = scanner.nextLine();
        Student student = findStudentById(schools, studentId);

        if (student == null) {
            System.out.println("Student not found. Please try again.");
            return;
        }

        System.out.println("Enter the new name of the student:");
        student.setName(scanner.nextLine());
        System.out.println("Enter the new age of the student:");
        student.setAge(getValidIntegerInput(scanner));
    }

    private static void listAllSchools(List<School> schools) {
        int totalStudents = 0;
        for (School school : schools) {
            school.printAllStudents();
            totalStudents += school.getStudents().size();
        }
        System.out.println("Total Schools: " + schools.size());
        System.out.println("Total Students: " + totalStudents);
    }

    private static void listBySchool(Scanner scanner, List<School> schools) {
        System.out.println("Enter the name or code of the school:");
        String schoolIdentifier = scanner.nextLine();
        School school = findSchoolByNameOrCode(schools, schoolIdentifier);

        if (school == null) {
            System.out.println("School not found. Available schools are:");
            for (School s : schools) {
                System.out.println("- " + s.getName() + " (Code: " + s.getSchoolCode() + ")");
            }
        } else {
            school.printAllStudents();
        }
    }

    private static void searchStudent(Scanner scanner, List<School> schools) {
        System.out.println("Enter the student name or ID to search:");
        String query = scanner.nextLine().trim().toLowerCase();

        boolean found = false;
        for (School school : schools) {
            for (Student student : school.getStudents()) {
                if (student.getName().toLowerCase().contains(query) || String.valueOf(student.getStudentId()).toLowerCase().contains(query)) {
                    System.out.println("Found student: " + student.getName() + " (ID: " + student.getStudentId() + ", Age: " + student.getAge() + ") in school: " + school.getName());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No student found with the given name or ID.");
        }
    }

    private static int getValidIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }

    private static School findSchoolByNameOrCode(List<School> schools, String identifier) {
        for (School school : schools) {
            if (school.getName().equalsIgnoreCase(identifier) || school.getSchoolCode().equalsIgnoreCase(identifier)) {
                return school;
            }
        }
        return null;
    }

    private static School findSchoolByCode(List<School> schools, String code) {
        for (School school : schools) {
            if (school.getSchoolCode().equalsIgnoreCase(code)) {
                return school;
            }
        }
        return null;
    }

    private static Student findStudentById(List<School> schools, String studentId) {
        for (School school : schools) {
            for (Student student : school.getStudents()) {
                if (String.valueOf(student.getStudentId()).equalsIgnoreCase(studentId)) {
                    return student;
                }
            }
        }
        return null;
    }

    private static void exportDataToCSV(List<School> schools) {
        System.out.println("Enter the CSV file name to export data:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine().trim();
        ExcelUtil.exportDataToCSV(fileName, schools);
        System.out.println("Data exported to " + fileName);
    }

    private static void exportStudentDataToExcel(List<School> schools) {
        System.out.println("Enter the Excel file name to export student data:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine().trim();
        ExcelUtil.saveSchoolDataToExcel(fileName, schools);
        System.out.println("Student data exported to " + fileName);
    }

    private static void exportStudentDataToSeparateExcel(List<School> schools) {
        System.out.println("Enter the Excel file name to export student data:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine().trim();
        ExcelUtil.saveStudentDataToSeparateExcel(fileName, schools);
        System.out.println("Student data exported to " + fileName);
    }
}