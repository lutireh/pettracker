package com.lutireh.pettracker.domain.model

enum class TaskType(val label: String) {
    WALK("Walk"),
    VACCINE("Vaccine"),
    FEED("Feed"),
    BATH("Bath"),
    VET("Vet Visit"),
    OTHER("Other")
}