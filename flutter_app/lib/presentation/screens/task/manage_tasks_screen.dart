import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:intl/intl.dart";
import "package:provider/provider.dart";

import "../../../domain/models/pet_task.dart";
import "../../state/app_state.dart";

class ManageTasksScreen extends StatelessWidget {
  const ManageTasksScreen({super.key, required this.petId});
  final int petId;

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    final pet = state.getPet(petId);
    final tasks = state.getTasksByPet(petId);

    return Scaffold(
      appBar: AppBar(title: Text("Tarefas - ${pet?.name ?? "Pet"}")),
      floatingActionButton: FloatingActionButton(
        onPressed: () => context.push("/add-task?petId=$petId"),
        child: const Icon(Icons.add),
      ),
      body: ListView.builder(
        itemCount: tasks.length,
        itemBuilder: (context, index) => _TaskRow(task: tasks[index]),
      ),
    );
  }
}

class _TaskRow extends StatelessWidget {
  const _TaskRow({required this.task});
  final PetTask task;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(task.type.label),
        subtitle: Text(DateFormat("dd/MM/yyyy").format(task.date)),
        trailing: PopupMenuButton<String>(
          onSelected: (value) async {
            if (value == "edit") context.push("/edit-task/${task.id}");
            if (value == "delete") await context.read<AppState>().deleteTask(task.id!);
          },
          itemBuilder: (_) => const [
            PopupMenuItem(value: "edit", child: Text("Editar")),
            PopupMenuItem(value: "delete", child: Text("Excluir")),
          ],
        ),
      ),
    );
  }
}
