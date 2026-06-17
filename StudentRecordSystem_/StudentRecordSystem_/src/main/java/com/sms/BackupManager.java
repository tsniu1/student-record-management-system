package com.sms;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class BackupManager {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String sourceFile;
    private final String backupFile;

    public BackupManager(String sourceFile, String backupFile) {
        this.sourceFile = sourceFile;
        this.backupFile = backupFile;
    }

    
    public boolean backup() {
        File src = new File(sourceFile);
        if (!src.exists()) {
            System.out.println("[Backup] Source file does not exist: " + sourceFile);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {

            
            writer.write("# ==");
            writer.newLine();
            writer.write("# BACKUP - Student Record Management System");
            writer.newLine();
            writer.write("# Backup Timestamp : " + SDF.format(new Date()));
            writer.newLine();
            writer.write("# Source File      : " + src.getAbsolutePath());
            writer.newLine();
            writer.write("# ==");
            writer.newLine();

       
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                lineCount++;
            }

            System.out.println("[Backup] Success — " + lineCount + " lines copied to: " + backupFile);
            return true;

        } catch (IOException e) {
            System.err.println("[Backup] Error during backup: " + e.getMessage());
            return false;
        }
    }

    
    public boolean restore() {
        File bak = new File(backupFile);
        if (!bak.exists()) {
            System.out.println("[Backup] No backup file found at: " + backupFile);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(bak));
             BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {

            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {
                
                if (skipHeader && line.startsWith("#")) {
                    continue;
                }
                skipHeader = false;         
                writer.write(line);
                writer.newLine();
            }

            System.out.println("[Backup] Restore complete. Source file updated: " + sourceFile);
            return true;

        } catch (IOException e) {
            System.err.println("[Backup] Error during restore: " + e.getMessage());
            return false;
        }
    }
}
