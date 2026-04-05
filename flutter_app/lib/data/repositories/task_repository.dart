import "../../domain/models/pet_task.dart";
import "../../domain/models/task_type.dart";
import "../local/app_database.dart";

class TaskRepository {
  Future<List<PetTask>> getAllTasks() async {
    final db = await AppDatabase.instance.database;
    final rows = await db.query("pet_activities", orderBy: "date ASC");
    return rows.map(_mapTask).toList();
  }

  Future<List<PetTask>> getTasksByPet(int petId) async {
    final db = await AppDatabase.instance.database;
    final rows =
        await db.query("pet_activities", where: "petId = ?", whereArgs: [petId], orderBy: "date DESC");
    return rows.map(_mapTask).toList();
  }

  Future<PetTask?> getTaskById(int taskId) async {
    final db = await AppDatabase.instance.database;
    final rows = await db.query("pet_activities", where: "id = ?", whereArgs: [taskId], limit: 1);
    if (rows.isEmpty) return null;
    return _mapTask(rows.first);
  }

  Future<int> addTask(PetTask task) async {
    final db = await AppDatabase.instance.database;
    return db.insert("pet_activities", {
      "petId": task.petId,
      "type": task.type.name,
      "date": task.date.millisecondsSinceEpoch,
      "notes": task.notes,
      "reminderTime": task.reminderTime?.millisecondsSinceEpoch,
    });
  }

  Future<void> updateTask(PetTask task) async {
    final db = await AppDatabase.instance.database;
    await db.update(
      "pet_activities",
      {
        "petId": task.petId,
        "type": task.type.name,
        "date": task.date.millisecondsSinceEpoch,
        "notes": task.notes,
        "reminderTime": task.reminderTime?.millisecondsSinceEpoch,
      },
      where: "id = ?",
      whereArgs: [task.id],
    );
  }

  Future<void> deleteTask(int taskId) async {
    final db = await AppDatabase.instance.database;
    await db.delete("pet_activities", where: "id = ?", whereArgs: [taskId]);
  }

  PetTask _mapTask(Map<String, Object?> row) {
    return PetTask(
      id: row["id"] as int,
      petId: row["petId"] as int,
      type: TaskType.fromName(row["type"] as String),
      date: DateTime.fromMillisecondsSinceEpoch(row["date"] as int),
      notes: row["notes"] as String?,
      reminderTime: row["reminderTime"] == null
          ? null
          : DateTime.fromMillisecondsSinceEpoch(row["reminderTime"] as int),
    );
  }
}
