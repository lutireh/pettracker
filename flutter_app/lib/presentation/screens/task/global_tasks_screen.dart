import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:intl/intl.dart";
import "package:provider/provider.dart";

import "../../../domain/models/pet_task.dart";
import "../../state/app_state.dart";

class GlobalTasksScreen extends StatelessWidget {
  const GlobalTasksScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    return Scaffold(
      appBar: AppBar(title: const Text("Tarefas")),
      floatingActionButton: FloatingActionButton(
        onPressed: () => context.push("/add-task"),
        child: const Icon(Icons.add),
      ),
      body: ListView.builder(
        itemCount: state.tasks.length,
        itemBuilder: (context, index) => _TaskTile(task: state.tasks[index]),
      ),
    );
  }
}

class _TaskTile extends StatelessWidget {
  const _TaskTile({required this.task});
  final PetTask task;

  @override
  Widget build(BuildContext context) {
    final state = context.read<AppState>();
    final pet = state.getPet(task.petId);
    return Card(
      child: ListTile(
        title: Text(task.type.label),
        subtitle: Text("${pet?.name ?? "Pet"} - ${DateFormat("dd/MM/yyyy").format(task.date)}"),
        onTap: () => context.push("/edit-task/${task.id}"),
      ),
    );
  }
}
