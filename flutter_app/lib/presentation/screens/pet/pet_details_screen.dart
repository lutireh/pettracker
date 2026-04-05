import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:provider/provider.dart";

import "../../state/app_state.dart";

class PetDetailsScreen extends StatelessWidget {
  const PetDetailsScreen({super.key, required this.petId});
  final int petId;

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    final pet = state.getPet(petId);
    if (pet == null) {
      return const Scaffold(body: Center(child: Text("Pet nao encontrado")));
    }

    return Scaffold(
      appBar: AppBar(title: Text(pet.name)),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("Raca: ${pet.breed ?? "-"}"),
            Text("Idade: ${pet.age?.toString() ?? "-"}"),
            Text("Peso: ${pet.weight?.toString() ?? "-"}"),
            const SizedBox(height: 16),
            Wrap(
              spacing: 10,
              children: [
                ElevatedButton(
                  onPressed: () => context.push("/edit-pet/${pet.id}"),
                  child: const Text("Editar"),
                ),
                ElevatedButton(
                  onPressed: () => context.push("/manage-tasks/${pet.id}"),
                  child: const Text("Gerenciar tarefas"),
                ),
                ElevatedButton(
                  onPressed: () async {
                    await context.read<AppState>().deletePet(pet.id!);
                    if (context.mounted) context.pop();
                  },
                  child: const Text("Excluir"),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
