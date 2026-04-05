import "package:flutter/material.dart";
import "package:provider/provider.dart";

import "data/repositories/pet_repository.dart";
import "data/repositories/task_repository.dart";
import "presentation/navigation/app_router.dart";
import "presentation/state/app_state.dart";

void main() {
  runApp(const PetTrackerApp());
}

class PetTrackerApp extends StatelessWidget {
  const PetTrackerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => AppState(PetRepository(), TaskRepository())..loadAll(),
      child: MaterialApp.router(
        debugShowCheckedModeBanner: false,
        title: "PetTracker",
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFFCB954A)),
          useMaterial3: true,
        ),
        routerConfig: buildRouter(),
      ),
    );
  }
}
