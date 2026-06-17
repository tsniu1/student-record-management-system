# System Design

Layers:
1. Model → Student
2. Service → StudentService
3. Storage → Text/Binary/Serialization handlers
4. Utilities → FileManager, BackupManager
5. UI → Main

Persistence:
- Text: Scanner + PrintWriter
- Binary: DataInput/OutputStream
- Serialization: ObjectInput/OutputStream