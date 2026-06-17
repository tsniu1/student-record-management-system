package com.sms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class that handles all filesystem bootstrap tasks using java.io.File.
 *
 * Responsibilities:
 *  - Ensure the data/ and backup/ directories exist (created automatically if absent).
 *  - Provide canonical paths for each storage file.
 *  - Display file metadata (name, absolute path, size, last-modified date).
 */
public class FileManager {

    // ── Directory paths ───────────────────────────────────────────────────────
    public static final String DATA_DIR   = "data";
    public static final String BACKUP_DIR = "backup";

    // ── File names ────────────────────────────────────────────────────────────
    public static final String TEXT_FILE   = DATA_DIR   + File.separator + "students.txt";
    public static final String BINARY_FILE = DATA_DIR   + File.separator + "students.dat";
    public static final String SERIAL_FILE = DATA_DIR   + File.separator + "students.ser";
    public static final String BACKUP_FILE = BACKUP_DIR + File.separator + "students_backup.txt";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ── Initialisation ────────────────────────────────────────────────────────

    /**
     * Creates the data/ and backup/ directories (and placeholder files) if they
     * do not already exist.  Called once at application start-up.
     */
    public static void initialise() {
        createDirectory(DATA_DIR);
        createDirectory(BACKUP_DIR);

        // Touch the text file so it exists for first-run displays
        ensureFileExists(TEXT_FILE);
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("[FileManager] Created directory: " + dir.getAbsolutePath());
            } else {
                System.err.println("[FileManager] Could not create directory: " + path);
            }
        }
    }

    private static void ensureFileExists(String path) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    System.out.println("[FileManager] Created file: " + f.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("[FileManager] Could not create file " + path + ": " + e.getMessage());
            }
        }
    }

    // ── File properties display ───────────────────────────────────────────────

    /**
     * Prints metadata for every managed file using the java.io.File API.
     */
    public static void displayAllFileProperties() {
        System.out.println("\n" + "=".repeat(65));
        System.out.println("  FILE PROPERTIES");
        System.out.println("=".repeat(65));
        displayProperties(new File(TEXT_FILE),   "Text File  ");
        displayProperties(new File(BINARY_FILE),  "Binary File");
        displayProperties(new File(SERIAL_FILE),  "Serial File");
        displayProperties(new File(BACKUP_FILE),  "Backup File");
    }

    /**
     * Prints metadata for a single file.
     *
     * @param file  the File to inspect
     * @param label human-readable label
     */
    public static void displayProperties(File file, String label) {
        System.out.println("\n  " + label + ":");
        System.out.println("  ├─ Name          : " + file.getName());
        System.out.println("  ├─ Absolute Path : " + file.getAbsolutePath());
        System.out.println("  ├─ Exists        : " + file.exists());
        System.out.println("  ├─ Size          : " + (file.exists() ? file.length() + " bytes" : "N/A"));
        System.out.println("  ├─ Is File       : " + file.isFile());
        System.out.println("  ├─ Is Directory  : " + file.isDirectory());
        System.out.println("  ├─ Can Read      : " + file.canRead());
        System.out.println("  ├─ Can Write     : " + file.canWrite());
        System.out.println("  └─ Last Modified : " +
            (file.exists() ? SDF.format(new Date(file.lastModified())) : "N/A"));
    }
}
