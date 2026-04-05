import "package:flutter_test/flutter_test.dart";
import "package:pettracker_flutter/main.dart";

void main() {
  testWidgets("App boots and shows shell", (tester) async {
    await tester.pumpWidget(const PetTrackerApp());
    await tester.pumpAndSettle();

    expect(find.text("Home"), findsOneWidget);
    expect(find.text("Meus Pets"), findsOneWidget);
    expect(find.text("Tarefas"), findsOneWidget);
  });
}
