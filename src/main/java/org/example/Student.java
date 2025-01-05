package org.example;

import java.util.List;

public class Student {
    private int id;
    private String name;
    private int age;
    private String studentId;
    private int schoolId; // Foreign key

    public Student(int id, String name, int age, String studentId, int schoolId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.schoolId = schoolId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public static boolean isStudentIdUnique(List<Student> students, String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return false;
            }
        }
        return true;
    }
}
