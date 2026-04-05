import "package:flutter/material.dart";
import "package:go_router/go_router.dart";

class ShellScreen extends StatelessWidget {
  const ShellScreen({super.key, required this.navigationShell});

  final StatefulNavigationShell navigationShell;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: navigationShell,
      bottomNavigationBar: NavigationBar(
        selectedIndex: navigationShell.currentIndex,
        destinations: const [
          NavigationDestination(icon: Icon(Icons.dashboard), label: "Home"),
          NavigationDestination(icon: Icon(Icons.pets), label: "Meus Pets"),
          NavigationDestination(icon: Icon(Icons.list_alt), label: "Tarefas"),
        ],
        onDestinationSelected: (index) => navigationShell.goBranch(index),
      ),
    );
  }
}
