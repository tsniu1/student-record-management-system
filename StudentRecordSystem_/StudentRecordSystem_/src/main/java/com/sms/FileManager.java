package com.sms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileManager {

    
    public static final String DATA_DIR   = "data";
    public static final String BACKUP_DIR = "backup";

    
    public static final String TEXT_FILE   = DATA_DIR   + File.separator + "students.txt";
    public static final String BINARY_FILE = DATA_DIR   + File.separator + "students.dat";
    public static final String SERIAL_FILE = DATA_DIR   + File.separator + "students.ser";
    public static final String BACKUP_FILE = BACKUP_DIR + File.separator + "students_backup.txt";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   
    public static void initialise() {
        createDirectory(DATA_DIR);
        createDirectory(BACKUP_DIR);

        
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

   
    public static void displayAllFileProperties() {
        System.out.println("\n" + "=".repeat(65));
        System.out.println("  FILE PROPERTIES");
        System.out.println("=".repeat(65));
        displayProperties(new File(TEXT_FILE),   "Text File  ");
        displayProperties(new File(BINARY_FILE),  "Binary File");
        displayProperties(new File(SERIAL_FILE),  "Serial File");
        displayProperties(new File(BACKUP_FILE),  "Backup File");
    }

   
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
