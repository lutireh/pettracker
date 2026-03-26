package com.lutireh.pettracker.domain.model

enum class TaskType(val label: String) {
    WALK("Passeio"),
    VACCINE("Vacina"),
    FEED("Alimentação"),
    BATH("Banho"),
    VET("Veterinário"),
    EXAM("Exame"),
    OTHER("Outro")
}