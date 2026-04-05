enum TaskType {
  walk("Passeio"),
  vaccine("Vacina"),
  feed("Alimentacao"),
  bath("Banho"),
  vet("Veterinario"),
  exam("Exame"),
  other("Outro");

  const TaskType(this.label);
  final String label;

  static TaskType fromName(String value) {
    return TaskType.values.firstWhere(
      (type) => type.name == value,
      orElse: () => TaskType.other,
    );
  }
}
