import "../../domain/models/pet.dart";
import "../local/app_database.dart";

class PetRepository {
  Future<List<Pet>> getAllPets() async {
    final db = await AppDatabase.instance.database;
    final rows = await db.query("pet", orderBy: "name ASC");
    return rows
        .map(
          (row) => Pet(
            id: row["id"] as int,
            name: row["name"] as String,
            breed: row["breed"] as String?,
            age: row["age"] as int?,
            weight: row["weight"] as int?,
            photoUri: row["photoUri"] as String?,
          ),
        )
        .toList();
  }

  Future<Pet?> getPetById(int petId) async {
    final db = await AppDatabase.instance.database;
    final rows = await db.query("pet", where: "id = ?", whereArgs: [petId], limit: 1);
    if (rows.isEmpty) return null;
    final row = rows.first;
    return Pet(
      id: row["id"] as int,
      name: row["name"] as String,
      breed: row["breed"] as String?,
      age: row["age"] as int?,
      weight: row["weight"] as int?,
      photoUri: row["photoUri"] as String?,
    );
  }

  Future<int> addPet(Pet pet) async {
    final db = await AppDatabase.instance.database;
    return db.insert("pet", {
      "name": pet.name,
      "breed": pet.breed,
      "age": pet.age,
      "weight": pet.weight,
      "photoUri": pet.photoUri,
    });
  }

  Future<void> updatePet(Pet pet) async {
    final db = await AppDatabase.instance.database;
    await db.update(
      "pet",
      {
        "name": pet.name,
        "breed": pet.breed,
        "age": pet.age,
        "weight": pet.weight,
        "photoUri": pet.photoUri,
      },
      where: "id = ?",
      whereArgs: [pet.id],
    );
  }

  Future<void> deletePet(int petId) async {
    final db = await AppDatabase.instance.database;
    await db.delete("pet", where: "id = ?", whereArgs: [petId]);
    await db.delete("pet_activities", where: "petId = ?", whereArgs: [petId]);
  }
}
