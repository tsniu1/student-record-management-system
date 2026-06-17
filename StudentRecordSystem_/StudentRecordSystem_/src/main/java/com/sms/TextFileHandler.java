package com.sms;

import java.io.*;
import java.util.*;

/**
 * Handles student persistence using plain text files.
 * Uses {@link Scanner} for reading and {@link PrintWriter} for writing,
 * both wrapped in {@link BufferedReader}/{@link BufferedWriter} for efficiency.
 *
 * File format (one record per line):
 *   StudentID|Name|Department|GPA
 */
public class TextFileHandler {

    private final String filePath;

    public TextFileHandler(String filePath) {
        this.filePath = filePath;
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    /**
     * Reads all student records from the text file.
     *
     * @return list of students; empty list if file is absent or empty
     */
    public List<Student> readAll() {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return students;
        }

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty() && !line.startsWith("#")) {   // skip blanks / comments
                    try {
                        students.add(Student.fromTextLine(line));
                    } catch (IllegalArgumentException e) {
                        System.err.println("[TextFileHandler] Skipping malformed line: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("[TextFileHandler] File not found: " + filePath);
        }

        return students;
    }

    // ── Write all ─────────────────────────────────────────────────────────────

    /**
     * Overwrites the text file with the supplied list of students.
     *
     * @param students list to persist
     */
    public void writeAll(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.println("# Student Records - Text File");
            writer.println("# Format: ID|Name|Department|GPA");
            writer.println("# Generated: " + new Date());
            writer.println();
            for (Student s : students) {
                writer.println(s.toTextLine());
            }
        } catch (IOException e) {
            System.err.println("[TextFileHandler] Error writing file: " + e.getMessage());
        }
    }

    // ── Convenience CRUD helpers ──────────────────────────────────────────────

    /** Appends a single student to the existing file. */
    public void append(Student student) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            writer.println(student.toTextLine());
        } catch (IOException e) {
            System.err.println("[TextFileHandler] Error appending record: " + e.getMessage());
        }
    }

    /** Returns the file path this handler operates on. */
    public String getFilePath() { return filePath; }
}
