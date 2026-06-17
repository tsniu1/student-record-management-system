package com.sms;

import java.io.Serializable;

/**
 * Student entity class.
 * Implements Serializable to support object serialization (ObjectInputStream / ObjectOutputStream).
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentId;
    private String name;
    private String department;
    private double gpa;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    public Student(String studentId, String name, String department, double gpa) {
        this.studentId  = studentId;
        this.name       = name;
        this.department = department;
        this.gpa        = gpa;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getStudentId()              { return studentId;  }
    public void   setStudentId(String id)     { this.studentId = id; }

    public String getName()                   { return name; }
    public void   setName(String name)        { this.name = name; }

    public String getDepartment()             { return department; }
    public void   setDepartment(String dept)  { this.department = dept; }

    public double getGpa()                    { return gpa; }
    public void   setGpa(double gpa)          { this.gpa = gpa; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns a pipe-delimited line suitable for text-file storage.
     * Format: ID|Name|Department|GPA
     */
    public String toTextLine() {
        return studentId + "|" + name + "|" + department + "|" + gpa;
    }

    /**
     * Reconstructs a Student from a pipe-delimited text line.
     */
    public static Student fromTextLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid student record line: " + line);
        }
        return new Student(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            Double.parseDouble(parts[3].trim())
        );
    }

    @Override
    public String toString() {
        return String.format(
            "Student{ID='%s', Name='%s', Department='%s', GPA=%.2f}",
            studentId, name, department, gpa
        );
    }

    /** Pretty-print a single student row for console tables. */
    public String toTableRow() {
        return String.format("| %-10s | %-20s | %-20s | %-5.2f |",
            studentId, name, department, gpa);
    }
}
