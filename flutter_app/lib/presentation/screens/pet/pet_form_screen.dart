import "package:flutter/material.dart";
import "package:go_router/go_router.dart";
import "package:provider/provider.dart";

import "../../../domain/models/pet.dart";
import "../../state/app_state.dart";

class PetFormScreen extends StatefulWidget {
  const PetFormScreen({super.key, this.petId});
  final int? petId;

  @override
  State<PetFormScreen> createState() => _PetFormScreenState();
}

class _PetFormScreenState extends State<PetFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameCtrl = TextEditingController();
  final _breedCtrl = TextEditingController();
  final _ageCtrl = TextEditingController();
  final _weightCtrl = TextEditingController();
  bool _initialized = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (_initialized) return;
    _initialized = true;
    final existing = widget.petId == null ? null : context.read<AppState>().getPet(widget.petId!);
    if (existing != null) {
      _nameCtrl.text = existing.name;
      _breedCtrl.text = existing.breed ?? "";
      _ageCtrl.text = existing.age?.toString() ?? "";
      _weightCtrl.text = existing.weight?.toString() ?? "";
    }
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _breedCtrl.dispose();
    _ageCtrl.dispose();
    _weightCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final isEdit = widget.petId != null;
    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? "Editar Pet" : "Adicionar Pet")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _nameCtrl,
                decoration: const InputDecoration(labelText: "Nome"),
                validator: (v) => (v == null || v.trim().isEmpty) ? "Informe o nome" : null,
              ),
              TextFormField(controller: _breedCtrl, decoration: const InputDecoration(labelText: "Raca")),
              TextFormField(
                controller: _ageCtrl,
                decoration: const InputDecoration(labelText: "Idade"),
                keyboardType: TextInputType.number,
              ),
              TextFormField(
                controller: _weightCtrl,
                decoration: const InputDecoration(labelText: "Peso"),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () async {
                  if (!_formKey.currentState!.validate()) return;
                  final state = context.read<AppState>();
                  final model = Pet(
                    id: widget.petId,
                    name: _nameCtrl.text.trim(),
                    breed: _breedCtrl.text.trim().isEmpty ? null : _breedCtrl.text.trim(),
                    age: int.tryParse(_ageCtrl.text.trim()),
                    weight: int.tryParse(_weightCtrl.text.trim()),
                  );
                  if (isEdit) {
                    await state.updatePet(model);
                  } else {
                    await state.addPet(model);
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
