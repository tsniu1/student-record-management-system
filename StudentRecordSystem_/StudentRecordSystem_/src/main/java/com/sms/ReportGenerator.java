package com.sms;

import java.util.*;
import java.util.stream.*;


public class ReportGenerator {

    private final List<Student> students;

    public ReportGenerator(List<Student> students) {
        this.students = students;
    }

    
    public void generate() {
        System.out.println("\n" + "=".repeat(65));
        System.out.println("              STUDENT RECORD MANAGEMENT SYSTEM");
        System.out.println("                      STATISTICAL REPORT");
        System.out.println("=".repeat(65));

        if (students.isEmpty()) {
            System.out.println("  No student records found.");
            System.out.println("=".repeat(65));
            return;
        }

        System.out.println("  Total Students  : " + getTotalStudents());
        System.out.println();

        Student highest = getHighestGpa();
        System.out.println("  Highest GPA     : " + String.format("%.2f", highest.getGpa()));
        System.out.println("    └─ " + highest.getName() + " (" + highest.getStudentId() + ") — " + highest.getDepartment());

        Student lowest = getLowestGpa();
        System.out.println();
        System.out.println("  Lowest GPA      : " + String.format("%.2f", lowest.getGpa()));
        System.out.println("    └─ " + lowest.getName() + " (" + lowest.getStudentId() + ") — " + lowest.getDepartment());

        System.out.println();
        System.out.println("  Average GPA     : " + String.format("%.2f", getAverageGpa()));

        System.out.println();
        System.out.println("  GPA Distribution:");
        System.out.println("    ├─ 3.5 – 4.0 (Excellent) : " + countInRange(3.5, 4.0));
        System.out.println("    ├─ 3.0 – 3.4 (Good)      : " + countInRange(3.0, 3.49));
        System.out.println("    ├─ 2.5 – 2.9 (Average)   : " + countInRange(2.5, 2.99));
        System.out.println("    └─ Below 2.5 (Below Avg) : " + countInRange(0.0, 2.49));

        System.out.println();
        System.out.println("  Department Breakdown:");
        getDepartmentCounts().forEach((dept, count) ->
            System.out.printf("    ├─ %-25s : %d student(s)%n", dept, count));

        System.out.println("=".repeat(65));
    }

    

    public int getTotalStudents() {
        return students.size();
    }

    public Student getHighestGpa() {
        return students.stream()
            .max(Comparator.comparingDouble(Student::getGpa))
            .orElseThrow(() -> new NoSuchElementException("No students present"));
    }

    public Student getLowestGpa() {
        return students.stream()
            .min(Comparator.comparingDouble(Student::getGpa))
            .orElseThrow(() -> new NoSuchElementException("No students present"));
    }

    public double getAverageGpa() {
        return students.stream()
            .mapToDouble(Student::getGpa)
            .average()
            .orElse(0.0);
    }

    private long countInRange(double min, double max) {
        return students.stream()
            .filter(s -> s.getGpa() >= min && s.getGpa() <= max)
            .count();
    }

    private Map<String, Long> getDepartmentCounts() {
        return students.stream()
            .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));
    }
}
