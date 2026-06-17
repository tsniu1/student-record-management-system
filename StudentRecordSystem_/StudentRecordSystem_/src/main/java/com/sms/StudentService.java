package com.sms;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Business-logic layer for student CRUD operations.
 *
 * Maintains an in-memory {@code List<Student>} as the working dataset and
 * synchronises changes to all three persistence stores on every write:
 *   1. Text file    (TextFileHandler  — Scanner / PrintWriter)
 *   2. Binary file  (BinaryFileHandler — DataInputStream / DataOutputStream)
 *   3. Serial file  (SerialFileHandler — ObjectInputStream / ObjectOutputStream)
 */
public class StudentService {

    private final List<Student>    students;
    private final TextFileHandler   textHandler;
    private final BinaryFileHandler binaryHandler;
    private final SerialFileHandler serialHandler;

    public StudentService(TextFileHandler   textHandler,
                          BinaryFileHandler binaryHandler,
                          SerialFileHandler serialHandler) {
        this.textHandler   = textHandler;
        this.binaryHandler = binaryHandler;
        this.serialHandler = serialHandler;
        this.students      = new ArrayList<>();

        // Bootstrap: load from text file (primary source of truth)
        this.students.addAll(textHandler.readAll());
    }

    // ── ADD ───────────────────────────────────────────────────────────────────

    /**
     * Adds a new student.  Rejects duplicates based on student ID.
     *
     * @param student student to add
     * @return true if added; false if ID already exists
     */
    public boolean addStudent(Student student) {
        if (findById(student.getStudentId()) != null) {
            System.out.println("  [Error] Student ID '" + student.getStudentId() + "' already exists.");
            return false;
        }
        if (!validateStudent(student)) {
            return false;
        }
        students.add(student);
        syncAll();
        System.out.println("  [OK] Student added: " + student);
        return true;
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────

    /**
     * Finds a student by ID (case-insensitive).
     *
     * @param id student ID to search
     * @return matching Student or null
     */
    public Student searchById(String id) {
        return students.stream()
            .filter(s -> s.getStudentId().equalsIgnoreCase(id.trim()))
            .findFirst()
            .orElse(null);
    }

    /** Alias used internally. */
    private Student findById(String id) {
        return searchById(id);
    }

    /**
     * Returns all students whose name contains the given substring (case-insensitive).
     */
    public List<Student> searchByName(String namePart) {
        return students.stream()
            .filter(s -> s.getName().toLowerCase().contains(namePart.trim().toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Returns all students in a given department (case-insensitive).
     */
    public List<Student> searchByDepartment(String department) {
        return students.stream()
            .filter(s -> s.getDepartment().equalsIgnoreCase(department.trim()))
            .collect(Collectors.toList());
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    /**
     * Updates the name, department, and GPA of an existing student.
     * The student ID itself is immutable.
     *
     * @param id          student to update
     * @param newName     new name (null or blank to keep existing)
     * @param newDept     new department (null or blank to keep existing)
     * @param newGpa      new GPA (&lt; 0 to keep existing)
     * @return true if found and updated; false otherwise
     */
    public boolean updateStudent(String id, String newName, String newDept, double newGpa) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("  [Error] Student ID '" + id + "' not found.");
            return false;
        }

        if (newName != null && !newName.isBlank())  s.setName(newName.trim());
        if (newDept != null && !newDept.isBlank())  s.setDepartment(newDept.trim());
        if (newGpa  >= 0)                           s.setGpa(newGpa);

        syncAll();
        System.out.println("  [OK] Student updated: " + s);
        return true;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * Deletes a student by ID.
     *
     * @param id student ID to delete
     * @return true if found and removed; false if not found
     */
    public boolean deleteStudent(String id) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("  [Error] Student ID '" + id + "' not found.");
            return false;
        }
        students.remove(s);
        syncAll();
        System.out.println("  [OK] Student deleted: " + s);
        return true;
    }

    // ── DISPLAY ───────────────────────────────────────────────────────────────

    /**
     * Prints a formatted table of all students to standard output.
     */
    public void displayAll() {
        System.out.println("\n" + "=".repeat(69));
        System.out.println("  ALL STUDENTS");
        System.out.println("=".repeat(69));

        if (students.isEmpty()) {
            System.out.println("  No student records found.");
        } else {
            String header = String.format("| %-10s | %-20s | %-20s | %-5s |",
                "ID", "Name", "Department", "GPA");
            String separator = "+" + "-".repeat(12) + "+" + "-".repeat(22) + "+" + "-".repeat(22) + "+" + "-".repeat(7) + "+";
            System.out.println(separator);
            System.out.println(header);
            System.out.println(separator);
            for (Student s : students) {
                System.out.println(s.toTableRow());
            }
            System.out.println(separator);
            System.out.println("  Total: " + students.size() + " student(s)");
        }
        System.out.println("=".repeat(69));
    }

    // ── GETTERS ───────────────────────────────────────────────────────────────

    /** Returns a defensive copy of the in-memory student list. */
    public List<Student> getAll() {
        return Collections.unmodifiableList(students);
    }

    // ── INTERNAL ─────────────────────────────────────────────────────────────

    /**
     * Writes the current in-memory list to all three persistence stores.
     * Called after every write operation to keep all stores in sync.
     */
    private void syncAll() {
        textHandler.writeAll(students);
        binaryHandler.writeAll(students);
        serialHandler.writeAll(students);
    }

    /** Basic validation: ID and name must not be blank; GPA must be 0.0 – 4.0. */
    private boolean validateStudent(Student s) {
        if (s.getStudentId() == null || s.getStudentId().isBlank()) {
            System.out.println("  [Validation] Student ID cannot be blank.");
            return false;
        }
        if (s.getName() == null || s.getName().isBlank()) {
            System.out.println("  [Validation] Name cannot be blank.");
            return false;
        }
        if (s.getDepartment() == null || s.getDepartment().isBlank()) {
            System.out.println("  [Validation] Department cannot be blank.");
            return false;
        }
        if (s.getGpa() < 0.0 || s.getGpa() > 4.0) {
            System.out.println("  [Validation] GPA must be between 0.0 and 4.0.");
            return false;
        }
        return true;
    }
}
