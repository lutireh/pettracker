package com.lutireh.pettracker.domain.model

data class PetTaskModel(
    val id: Int = 0,
    val petId: String,
    val type: TaskType,
    val date: String? = null, // TODO remove this nullable
    val notes: String? = null,
    val reminderTime: String? = null
)