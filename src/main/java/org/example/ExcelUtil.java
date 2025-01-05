package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static List<School> loadSchoolDataFromExcel(String schoolFileName, String studentFileName) {
        List<School> schools = new ArrayList<>();
        try (FileInputStream schoolFileIn = new FileInputStream(schoolFileName);
             Workbook schoolWorkbook = new XSSFWorkbook(schoolFileIn)) {

            // Load schools
            Sheet schoolSheet = schoolWorkbook.getSheetAt(0);
            for (Row row : schoolSheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String schoolCode = row.getCell(2).getStringCellValue();
                String address = row.getCell(3).getStringCellValue();

                School school = new School(id, name, schoolCode, address);
                schools.add(school);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load students from separate file
        try (FileInputStream studentFileIn = new FileInputStream(studentFileName);
             Workbook studentWorkbook = new XSSFWorkbook(studentFileIn)) {

            Sheet studentSheet = studentWorkbook.getSheetAt(0);
            for (Row row : studentSheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                int studentId = (int) row.getCell(0).getNumericCellValue();
                String studentName = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue();
                int schoolId = (int) row.getCell(3).getNumericCellValue();

                School school = findSchoolById(schools, schoolId);
                if (school != null) {
                    Student student = new Student(studentId, studentName, age, String.valueOf(studentId), schoolId);
                    school.addStudent(student);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return schools;
    }

    private static School findSchoolById(List<School> schools, int id) {
        for (School school : schools) {
            if (school.getId() == id) {
                return school;
            }
        }
        return null;
    }

    public static void saveSchoolDataToExcel(String fileName, List<School> schools) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("School Data");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("School ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Code");
        headerRow.createCell(3).setCellValue("Address");

        for (School school : schools) {
            Row schoolRow = sheet.createRow(rowNum++);
            schoolRow.createCell(0).setCellValue(school.getId());
            schoolRow.createCell(1).setCellValue(school.getName());
            schoolRow.createCell(2).setCellValue(school.getSchoolCode());
            schoolRow.createCell(3).setCellValue(school.getAddress());
        }

        // Auto-size columns to fit the content
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveStudentDataToSeparateExcel(String fileName, List<School> schools) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Data");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Student ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Age");
        headerRow.createCell(3).setCellValue("School ID"); // Foreign key

        int studentIdCounter = 1;
        for (School school : schools) {
            for (Student student : school.getStudents()) {
                Row studentRow = sheet.createRow(rowNum++);
                studentRow.createCell(0).setCellValue(studentIdCounter++);
                studentRow.createCell(1).setCellValue(student.getName());
                studentRow.createCell(2).setCellValue(student.getAge());
                studentRow.createCell(3).setCellValue(school.getId());
            }
        }

        // Auto-size columns to fit the content
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportDataToCSV(String fileName, List<School> schools) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportDataToCSV'");
    }
}