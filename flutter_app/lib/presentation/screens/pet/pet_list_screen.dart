import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:provider/provider.dart";

import "../../../domain/models/pet.dart";
import "../../state/app_state.dart";

class PetListScreen extends StatelessWidget {
  const PetListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();

    return Scaffold(
      appBar: AppBar(title: const Text("Meus Pets")),
      floatingActionButton: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          FloatingActionButton(
            heroTag: "add-pet",
            onPressed: () => context.push("/add-pet"),
            child: const Icon(Icons.add),
          ),
          const SizedBox(height: 12),
          FloatingActionButton(
            heroTag: "add-task",
            onPressed: () => context.push("/add-task"),
            child: const Icon(Icons.assignment_add),
          ),
        ],
      ),
      body: RefreshIndicator(
        onRefresh: state.loadAll,
        child: ListView.builder(
          itemCount: state.pets.length,
          itemBuilder: (context, index) {
            final pet = state.pets[index];
            return _PetTile(pet: pet);
          },
        ),
      ),
    );
  }
}

class _PetTile extends StatelessWidget {
  const _PetTile({required this.pet});
  final Pet pet;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(pet.name),
        subtitle: Text(pet.breed ?? "-"),
        onTap: () => context.push("/pet-details/${pet.id}"),
      ),
    );
  }
}
