package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class School {
    private int id;
    private String name;
    private String schoolCode;
    private String address;
    private List<Student> students;

    public School(int id, String name, String schoolCode, String address) {
        this.id = id;
        this.name = name;
        this.schoolCode = schoolCode;
        this.address = address;
        this.students = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        if (Student.isStudentIdUnique(students, student.getStudentId())) {
            this.students.add(student);
        } else {
            System.out.println("Student ID " + student.getStudentId() + " already exists. Please use a unique ID.");
        }
    }

    public void deleteStudentById(String studentId) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        boolean deleted = false;

        while (attempts < 3 && !deleted) {
            for (Student student : students) {
                if (student.getStudentId().equals(studentId)) {
                    students.remove(student);
                    System.out.println("Student with ID " + studentId + " has been deleted.");
                    deleted = true;
                    break;
                }
            }

            if (!deleted) {
                attempts++;
                if (attempts < 3) {
                    System.out.println("Student ID " + studentId + " does not exist. Please try again:");
                    studentId = scanner.nextLine();
                } else {
                    System.out.println("Student ID " + studentId + " does not exist. Execution terminated.");
                }
            }
        }
    }

    public void printAllStudents() {
        System.out.println("SCHOOL: " + name.toUpperCase() + " (CODE: " + schoolCode.toUpperCase() + ")");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-5s%n", "STUDENT ID", "NAME", "AGE");
        System.out.println("--------------------------------------------------");
        for (Student student : students) {
            System.out.printf("%-10s | %-20s | %-5d%n", student.getStudentId(), student.getName(), student.getAge());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Total Students: " + students.size());
        System.out.println("\n\n\n"); // Multiple empty new lines to separate schools
    }
}
