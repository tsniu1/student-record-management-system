package com.sms;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static final Scanner IN = new Scanner(System.in);

    private static StudentService   service;
    private static BackupManager    backup;

   ────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {


        FileManager.initialise();


        TextFileHandler   textHandler   = new TextFileHandler(FileManager.TEXT_FILE);
        BinaryFileHandler binaryHandler = new BinaryFileHandler(FileManager.BINARY_FILE);
        SerialFileHandler serialHandler = new SerialFileHandler(FileManager.SERIAL_FILE);

        service = new StudentService(textHandler, binaryHandler, serialHandler);
        backup  = new BackupManager(FileManager.TEXT_FILE, FileManager.BACKUP_FILE);

        
        seedDemoData();

        
        System.out.println("\n  Welcome to the Student Record Management System");
        menuLoop();
    }

  ──

    private static void menuLoop() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("  Enter choice: ");
            System.out.println();

            switch (choice) {
                case 1  -> menuAddStudent();
                case 2  -> menuSearchStudent();
                case 3  -> menuUpdateStudent();
                case 4  -> menuDeleteStudent();
                case 5  -> service.displayAll();
                case 6  -> new ReportGenerator(service.getAll()).generate();
                case 7  -> menuBackup();
                case 8  -> FileManager.displayAllFileProperties();
                case 9  -> menuDemoIO();
                case 0  -> { running = false; System.out.println("  Goodbye!"); }
                default -> System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n" + "─".repeat(45));
        System.out.println("   STUDENT RECORD MANAGEMENT SYSTEM — MENU");
        System.out.println("─".repeat(45));
        System.out.println("   1. Add Student");
        System.out.println("   2. Search Student by ID");
        System.out.println("   3. Update Student");
        System.out.println("   4. Delete Student");
        System.out.println("   5. Display All Students");
        System.out.println("   6. Generate Report");
        System.out.println("   7. Backup Records");
        System.out.println("   8. View File Properties");
        System.out.println("   9. Demo – Read Binary / Serial files");
        System.out.println("   0. Exit");
        System.out.println("─".repeat(45));
    }

   

    private static void menuAddStudent() {
        System.out.println("  ── Add Student ──");
        String id   = readString("  Student ID   : ");
        String name = readString("  Name         : ");
        String dept = readString("  Department   : ");
        double gpa  = readDouble("  GPA (0.0-4.0): ");
        service.addStudent(new Student(id, name, dept, gpa));
    }

    private static void menuSearchStudent() {
        System.out.println("  ── Search Student ──");
        System.out.println("  1. Search by ID");
        System.out.println("  2. Search by Name");
        System.out.println("  3. Search by Department");
        int choice = readInt("  Choose: ");

        switch (choice) {
            case 1 -> {
                String id = readString("  Enter Student ID: ");
                Student s = service.searchById(id);
                if (s != null) {
                    printHeader();
                    System.out.println(s.toTableRow());
                    printSeparator();
                } else {
                    System.out.println("  No student found with ID: " + id);
                }
            }
            case 2 -> {
                String name = readString("  Enter name (partial): ");
                List<Student> results = service.searchByName(name);
                printStudentList(results, "Search results for name: " + name);
            }
            case 3 -> {
                String dept = readString("  Enter department: ");
                List<Student> results = service.searchByDepartment(dept);
                printStudentList(results, "Search results for dept: " + dept);
            }
            default -> System.out.println("  Invalid option.");
        }
    }

    private static void menuUpdateStudent() {
        System.out.println("  ── Update Student ──");
        String id = readString("  Enter Student ID to update: ");

        Student existing = service.searchById(id);
        if (existing == null) {
            System.out.println("  Student not found: " + id);
            return;
        }

        System.out.println("  Current: " + existing);
        System.out.println("  (Press ENTER to keep existing value)");

        String name = readStringOptional("  New Name       [" + existing.getName() + "]: ");
        String dept = readStringOptional("  New Department [" + existing.getDepartment() + "]: ");
        String gpaStr = readStringOptional("  New GPA        [" + existing.getGpa() + "]: ");

        double newGpa = gpaStr.isBlank() ? -1 : Double.parseDouble(gpaStr);
        service.updateStudent(id, name, dept, newGpa);
    }

    private static void menuDeleteStudent() {
        System.out.println("  ── Delete Student ──");
        String id = readString("  Enter Student ID to delete: ");
        System.out.print("  Are you sure? (y/n): ");
        String confirm = IN.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            service.deleteStudent(id);
        } else {
            System.out.println("  Delete cancelled.");
        }
    }

    private static void menuBackup() {
        System.out.println("  ── Backup / Restore ──");
        System.out.println("  1. Create Backup");
        System.out.println("  2. Restore from Backup");
        int choice = readInt("  Choose: ");
        if (choice == 1)      backup.backup();
        else if (choice == 2) backup.restore();
        else                  System.out.println("  Invalid option.");
    }

    private static void menuDemoIO() {
        System.out.println("\n  ── Demo: Reading from Binary & Serial files ──");

        System.out.println("\n  [Binary File] Contents:");
        BinaryFileHandler bh = new BinaryFileHandler(FileManager.BINARY_FILE);
        List<Student> fromBinary = bh.readAll();
        printStudentList(fromBinary, "Binary file");

        System.out.println("\n  [Serial File] Contents:");
        SerialFileHandler sh = new SerialFileHandler(FileManager.SERIAL_FILE);
        List<Student> fromSerial = sh.readAll();
        printStudentList(fromSerial, "Serial file");
    }

    

    private static void printStudentList(List<Student> list, String label) {
        System.out.println("\n  " + label + " (" + list.size() + " record(s)):");
        if (list.isEmpty()) {
            System.out.println("  No records.");
        } else {
            printHeader();
            for (Student s : list) System.out.println(s.toTableRow());
            printSeparator();
        }
    }

    private static String sep = "+" + "-".repeat(12) + "+" + "-".repeat(22) + "+" + "-".repeat(22) + "+" + "-".repeat(7) + "+";

    private static void printHeader() {
        System.out.println(sep);
        System.out.printf("| %-10s | %-20s | %-20s | %-5s |%n", "ID", "Name", "Department", "GPA");
        System.out.println(sep);
    }

    private static void printSeparator() {
        System.out.println(sep);
    }

  
    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("  Input cannot be blank.");
        }
    }

   
    private static String readStringOptional(String prompt) {
        System.out.print(prompt);
        return IN.nextLine().trim();
    }

    
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(IN.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a whole number.");
            }
        }
    }

 
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(IN.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number (e.g. 3.75).");
            }
        }
    }

    
    private static void seedDemoData() {
        if (!service.getAll().isEmpty()) return;   

        System.out.println("  [Seed] Adding demo student records...");
        service.addStudent(new Student("S001", "Alice Johnson",   "Computer Science", 3.90));
        service.addStudent(new Student("S002", "Bob Martinez",    "Electrical Eng",   3.45));
        service.addStudent(new Student("S003", "Carol White",     "Mathematics",      3.78));
        service.addStudent(new Student("S004", "David Brown",     "Physics",          2.95));
        service.addStudent(new Student("S005", "Eve Thompson",    "Computer Science", 3.55));
        service.addStudent(new Student("S006", "Frank Wilson",    "Chemistry",        2.80));
        service.addStudent(new Student("S007", "Grace Lee",       "Mathematics",      3.92));
        service.addStudent(new Student("S008", "Hank Davis",      "Electrical Eng",   3.10));
        service.addStudent(new Student("S009", "Ivy Anderson",    "Physics",          3.67));
        service.addStudent(new Student("S010", "Jake Taylor",     "Chemistry",        2.50));
        System.out.println("  [Seed] 10 demo records added.\n");
    }
}
