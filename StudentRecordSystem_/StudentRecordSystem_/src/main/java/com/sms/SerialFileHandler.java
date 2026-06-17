package com.sms;

import java.io.*;
import java.util.*;

/**
 * Handles student persistence via Java Object Serialization.
 * Uses {@link ObjectOutputStream} for writing the entire list as a single
 * serialised object, and {@link ObjectInputStream} to restore it.
 *
 * Both streams are buffered to improve I/O performance.
 */
public class SerialFileHandler {

    private final String filePath;

    public SerialFileHandler(String filePath) {
        this.filePath = filePath;
    }

    // ── Write all ─────────────────────────────────────────────────────────────

    /**
     * Serialises the complete student list to the .ser file.
     *
     * @param students list to persist
     */
    @SuppressWarnings("unchecked")
    public void writeAll(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {

            oos.writeObject(students);

        } catch (IOException e) {
            System.err.println("[SerialFileHandler] Error serialising students: " + e.getMessage());
        }
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    /**
     * De-serialises the student list from the .ser file.
     *
     * @return list of students; empty list if file is absent or corrupted
     */
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
