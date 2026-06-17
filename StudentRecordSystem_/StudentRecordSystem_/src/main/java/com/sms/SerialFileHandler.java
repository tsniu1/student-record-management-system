package com.sms;

import java.io.*;
import java.util.*;


public class SerialFileHandler {

    private final String filePath;

    public SerialFileHandler(String filePath) {
        this.filePath = filePath;
    }

    
    @SuppressWarnings("unchecked")
    public void writeAll(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {

            oos.writeObject(students);

        } catch (IOException e) {
            System.err.println("[SerialFileHandler] Error serialising students: " + e.getMessage());
        }
    }

    
    @SuppressWarnings("unchecked")
    public List<Student> readAll() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {

            return (List<Student>) ois.readObject();

        } catch (ClassNotFoundException e) {
            System.err.println("[SerialFileHandler] Class not found during deserialisation: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[SerialFileHandler] Error reading serialised file: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /** Returns the file path this handler operates on. */
    public String getFilePath() { return filePath; }
}
