# Student Record Management System

## Features
- Add Student
- Search Student by ID
- Update Student
- Delete Student
- Display Students
- Reports (Total, Highest, Lowest, Average GPA)
- Text / Binary / Serialized Storage
- Buffered Backup
- Exception Handling

## Standard Git Structure
```
StudentRecordSystem_GitReady/
├── src/main/java/com/sms/
├── data/
│   ├── text/
│   ├── binary/
│   └── serialized/
├── backup/
├── docs/
├── README.md
└── .gitignore
```

## Run
javac src/main/java/com/sms/*.java
java -cp src/main/java com.sms.Main