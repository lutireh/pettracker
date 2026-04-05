class Pet {
  const Pet({
    this.id,
    required this.name,
    this.breed,
    this.age,
    this.weight,
    this.photoUri,
  });

  final int? id;
  final String name;
  final String? breed;
  final int? age;
  final int? weight;
  final String? photoUri;

  Pet copyWith({
    int? id,
    String? name,
    String? breed,
    int? age,
    int? weight,
    String? photoUri,
  }) {
    return Pet(
      id: id ?? this.id,
      name: name ?? this.name,
      breed: breed ?? this.breed,
      age: age ?? this.age,
      weight: weight ?? this.weight,
      photoUri: photoUri ?? this.photoUri,
    );
  }
}
