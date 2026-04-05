import "package:flutter/material.dart";
import "package:provider/provider.dart";

import "../state/app_state.dart";

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    final byType = state.taskCountByType.entries.toList()
      ..sort((a, b) => b.value.compareTo(a.value));

    return RefreshIndicator(
      onRefresh: state.loadAll,
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text("Dashboard", style: Theme.of(context).textTheme.headlineSmall),
          const SizedBox(height: 16),
          _MetricCard(label: "Pets cadastrados", value: state.totalPets.toString()),
          _MetricCard(label: "Total de tarefas", value: state.totalTasks.toString()),
          _MetricCard(label: "Tarefas vencidas", value: state.overdueTasks.toString()),
          const SizedBox(height: 12),
          Text("Tarefas por tipo", style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 8),
          if (byType.isEmpty)
            const Card(child: Padding(padding: EdgeInsets.all(16), child: Text("Sem tarefas por enquanto.")))
          else
            ...byType.map(
              (entry) => ListTile(
                title: Text(entry.key.label),
                trailing: Text(entry.value.toString()),
              ),
            ),
        ],
      ),
    );
  }
}

class _MetricCard extends StatelessWidget {
  const _MetricCard({required this.label, required this.value});

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(label),
        trailing: Text(value, style: Theme.of(context).textTheme.titleLarge),
      ),
    );
  }
}
