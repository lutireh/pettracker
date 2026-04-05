import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:intl/intl.dart";
import "package:provider/provider.dart";

import "../../../domain/models/pet_task.dart";
import "../../../domain/models/task_type.dart";
import "../../state/app_state.dart";

class TaskFormScreen extends StatefulWidget {
  const TaskFormScreen({super.key, this.taskId, this.petId});
  final int? taskId;
  final int? petId;

  @override
  State<TaskFormScreen> createState() => _TaskFormScreenState();
}

class _TaskFormScreenState extends State<TaskFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _notesCtrl = TextEditingController();
  TaskType _type = TaskType.walk;
  DateTime _date = DateTime.now();
  int? _petId;
  bool _initialized = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (_initialized) return;
    _initialized = true;
    final appState = context.read<AppState>();
    final task = widget.taskId == null ? null : appState.getTask(widget.taskId!);
    if (task != null) {
      _type = task.type;
      _date = task.date;
      _notesCtrl.text = task.notes ?? "";
      _petId = task.petId;
    } else {
      _petId = widget.petId ?? (appState.pets.isNotEmpty ? appState.pets.first.id : null);
    }
  }

  @override
  void dispose() {
    _notesCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final appState = context.watch<AppState>();
    final isEdit = widget.taskId != null;
    final pets = appState.pets;
    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? "Editar Tarefa" : "Adicionar Tarefa")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              DropdownButtonFormField<int>(
                value: _petId,
                decoration: const InputDecoration(labelText: "Pet"),
                items: pets
                    .map((pet) => DropdownMenuItem<int>(value: pet.id, child: Text(pet.name)))
                    .toList(),
                onChanged: (value) => setState(() => _petId = value),
                validator: (value) => value == null ? "Selecione um pet" : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<TaskType>(
                value: _type,
                decoration: const InputDecoration(labelText: "Tipo"),
                items: TaskType.values
                    .map((type) => DropdownMenuItem<TaskType>(value: type, child: Text(type.label)))
                    .toList(),
                onChanged: (value) => setState(() => _type = value ?? TaskType.other),
              ),
              const SizedBox(height: 12),
              ListTile(
                contentPadding: EdgeInsets.zero,
                title: Text("Data: ${DateFormat("dd/MM/yyyy").format(_date)}"),
                trailing: const Icon(Icons.calendar_month),
                onTap: () async {
                  final picked = await showDatePicker(
                    context: context,
                    initialDate: _date,
                    firstDate: DateTime(2020),
                    lastDate: DateTime(2100),
                  );
                  if (picked != null) setState(() => _date = picked);
                },
              ),
              TextFormField(
                controller: _notesCtrl,
                maxLines: 3,
                decoration: const InputDecoration(labelText: "Observacoes"),
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () async {
                  if (!_formKey.currentState!.validate()) return;
                  final model = PetTask(
                    id: widget.taskId,
                    petId: _petId!,
                    type: _type,
                    date: _date,
                    notes: _notesCtrl.text.trim().isEmpty ? null : _notesCtrl.text.trim(),
                  );
                  if (isEdit) {
                    await appState.updateTask(model);
                  } else {
                    await appState.addTask(model);
                  }
                  if (context.mounted) context.pop();
                },
                child: Text(isEdit ? "Salvar" : "Cadastrar"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
