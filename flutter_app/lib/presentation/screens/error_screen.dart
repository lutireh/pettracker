import "package:flutter/material.dart";
import "package:go_router/go_router.dart";

class ErrorScreen extends StatelessWidget {
  const ErrorScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Erro")),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text("Algo deu errado. Tente novamente."),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () => context.pop(),
              child: const Text("Voltar"),
            ),
          ],
        ),
      ),
    );
  }
}
