import "package:flutter/foundation.dart";

import "../../data/repositories/pet_repository.dart";
import "../../data/repositories/task_repository.dart";
import "../../domain/models/pet.dart";
import "../../domain/models/pet_task.dart";
import "../../domain/models/task_type.dart";

class AppState extends ChangeNotifier {
  AppState(this._petRepository, this._taskRepository);

  final PetRepository _petRepository;
  final TaskRepository _taskRepository;

  List<Pet> pets = [];
  List<PetTask> tasks = [];
  bool isLoading = false;
  String? errorMessage;

  Future<void> loadAll() async {
    isLoading = true;
    errorMessage = null;
    notifyListeners();
    try {
      pets = await _petRepository.getAllPets();
      tasks = await _taskRepository.getAllTasks();
    } catch (e) {
      errorMessage = e.toString();
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  Future<void> addPet(Pet pet) async {
    await _petRepository.addPet(pet);
    await loadAll();
  }

  Future<void> updatePet(Pet pet) async {
    await _petRepository.updatePet(pet);
    await loadAll();
  }

  Future<void> deletePet(int petId) async {
    await _petRepository.deletePet(petId);
    await loadAll();
  }

  Future<void> addTask(PetTask task) async {
    await _taskRepository.addTask(task);
    await loadAll();
  }

  Future<void> updateTask(PetTask task) async {
    await _taskRepository.updateTask(task);
    await loadAll();
  }

  Future<void> deleteTask(int taskId) async {
    await _taskRepository.deleteTask(taskId);
    await loadAll();
  }

  Pet? getPet(int id) {
    return pets.where((p) => p.id == id).cast<Pet?>().firstWhere((p) => p != null, orElse: () => null);
  }

  PetTask? getTask(int id) {
    return tasks.where((t) => t.id == id).cast<PetTask?>().firstWhere((t) => t != null, orElse: () => null);
  }

  List<PetTask> getTasksByPet(int petId) => tasks.where((task) => task.petId == petId).toList();

  int get totalPets => pets.length;
  int get totalTasks => tasks.length;
  int get overdueTasks => tasks.where((task) => task.date.isBefore(DateTime.now())).length;
  Map<TaskType, int> get taskCountByType {
    final result = <TaskType, int>{};
    for (final task in tasks) {
      result.update(task.type, (value) => value + 1, ifAbsent: () => 1);
    }
    return result;
  }
}
