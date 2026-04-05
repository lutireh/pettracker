import "package:path/path.dart";
import "package:sqflite/sqflite.dart";

class AppDatabase {
  AppDatabase._();
  static final AppDatabase instance = AppDatabase._();

  Database? _db;

  Future<Database> get database async {
    if (_db != null) return _db!;
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, "pettracker_flutter.db");
    _db = await openDatabase(
      path,
      version: 1,
      onCreate: (db, version) async {
        await db.execute("""
          CREATE TABLE pet(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            breed TEXT,
            age INTEGER,
            weight INTEGER,
            photoUri TEXT
          )
        """);
        await db.execute("""
          CREATE TABLE pet_activities(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            petId INTEGER NOT NULL,
            type TEXT NOT NULL,
            date INTEGER NOT NULL,
            notes TEXT,
            reminderTime INTEGER
          )
        """);
      },
    );
    return _db!;
  }
}
