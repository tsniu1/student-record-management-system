package com.sms;

import java.io.*;
import java.util.*;


public class BinaryFileHandler {

    private final String filePath;

    public BinaryFileHandler(String filePath) {
        this.filePath = filePath;
    }

    
    public void writeAll(List<Student> students) {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {

            dos.writeInt(students.size());         
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
