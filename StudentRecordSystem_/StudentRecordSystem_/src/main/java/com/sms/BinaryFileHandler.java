package com.sms;

import java.io.*;
import java.util.*;

/**
 * Handles student persistence using binary files.
 * Uses {@link DataOutputStream} (wrapped in {@link BufferedOutputStream}) for writing
 * and {@link DataInputStream} (wrapped in {@link BufferedInputStream}) for reading.
 *
 * Binary record layout (per student, fixed order):
 *   UTF  – studentId
 *   UTF  – name
 *   UTF  – department
 *   double – gpa
 */
public class BinaryFileHandler {

    private final String filePath;

    public BinaryFileHandler(String filePath) {
        this.filePath = filePath;
    }

    // ── Write all ─────────────────────────────────────────────────────────────

    /**
     * Serialises all students to a binary file using DataOutputStream.
     *
     * @param students list to persist
     */
    public void writeAll(List<Student> students) {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {

            dos.writeInt(students.size());          // record count header
            for (Student s : students) {
                dos.writeUTF(s.getStudentId());
                dos.writeUTF(s.getName());
                dos.writeUTF(s.getDepartment());
                dos.writeDouble(s.getGpa());
            }

        } catch (IOException e) {
            System.err.println("[BinaryFileHandler] Error writing binary file: " + e.getMessage());
        }
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    /**
     * De-serialises all students from the binary file using DataInputStream.
     *
     * @return list of students; empty list if file is absent or empty
     */
    public List<Student> readAll() {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return students;
        }

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {

            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                String id   = dis.readUTF();
                String name = dis.readUTF();
                String dept = dis.readUTF();
                double gpa  = dis.readDouble();
                students.add(new Student(id, name, dept, gpa));
            }

        } catch (EOFException e) {
            System.err.println("[BinaryFileHandler] Unexpected end of binary file.");
        } catch (IOException e) {
            System.err.println("[BinaryFileHandler] Error reading binary file: " + e.getMessage());
        }

        return students;
    }

    /** Returns the file path this handler operates on. */
    public String getFilePath() { return filePath; }
}
