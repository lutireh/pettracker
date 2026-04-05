import "task_type.dart";

class PetTask {
  const PetTask({
    this.id,
    required this.petId,
    required this.type,
    required this.date,
    this.notes,
    this.reminderTime,
  });

  final int? id;
  final int petId;
  final TaskType type;
  final DateTime date;
  final String? notes;
  final DateTime? reminderTime;

  PetTask copyWith({
    int? id,
    int? petId,
    TaskType? type,
    DateTime? date,
    String? notes,
    DateTime? reminderTime,
  }) {
    return PetTask(
      id: id ?? this.id,
      petId: petId ?? this.petId,
      type: type ?? this.type,
      date: date ?? this.date,
      notes: notes ?? this.notes,
      reminderTime: reminderTime ?? this.reminderTime,
    );
  }
}
