package com.sms;

import java.io.*;
import java.util.*;


public class TextFileHandler {

    private final String filePath;

    public TextFileHandler(String filePath) {
        this.filePath = filePath;
    }

    
    public List<Student> readAll() {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return students;
        }

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty() && !line.startsWith("#")) {   
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
