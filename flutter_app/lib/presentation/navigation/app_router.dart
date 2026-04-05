import "package:flutter/material.dart";
import "package:go_router/go_router.dart";

import "../screens/error_screen.dart";
import "../screens/home_screen.dart";
import "../screens/pet/pet_details_screen.dart";
import "../screens/pet/pet_form_screen.dart";
import "../screens/pet/pet_list_screen.dart";
import "../screens/shell_screen.dart";
import "../screens/task/global_tasks_screen.dart";
import "../screens/task/manage_tasks_screen.dart";
import "../screens/task/task_form_screen.dart";

GoRouter buildRouter() {
  return GoRouter(
    initialLocation: "/home",
    routes: [
      StatefulShellRoute.indexedStack(
        builder: (context, state, navShell) => ShellScreen(navigationShell: navShell),
        branches: [
          StatefulShellBranch(routes: [GoRoute(path: "/home", builder: (_, __) => const HomeScreen())]),
          StatefulShellBranch(
            routes: [GoRoute(path: "/pet-list", builder: (_, __) => const PetListScreen())],
          ),
          StatefulShellBranch(
            routes: [GoRoute(path: "/global-tasks", builder: (_, __) => const GlobalTasksScreen())],
          ),
        ],
      ),
      GoRoute(path: "/add-pet", builder: (_, __) => const PetFormScreen()),
      GoRoute(
        path: "/edit-pet/:petId",
        builder: (_, state) => PetFormScreen(petId: int.tryParse(state.pathParameters["petId"] ?? "")),
      ),
      GoRoute(
        path: "/pet-details/:petId",
        builder: (_, state) => PetDetailsScreen(petId: int.parse(state.pathParameters["petId"]!)),
      ),
      GoRoute(
        path: "/add-task",
        builder: (_, state) => TaskFormScreen(petId: int.tryParse(state.uri.queryParameters["petId"] ?? "")),
      ),
      GoRoute(
        path: "/edit-task/:taskId",
        builder: (_, state) => TaskFormScreen(taskId: int.tryParse(state.pathParameters["taskId"] ?? "")),
      ),
      GoRoute(
        path: "/manage-tasks/:petId",
        builder: (_, state) => ManageTasksScreen(petId: int.parse(state.pathParameters["petId"]!)),
      ),
      GoRoute(path: "/error", builder: (_, __) => const ErrorScreen()),
    ],
  );
}
