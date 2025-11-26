package com.lutireh.pettracker.domain.model

data class PetTaskModel(
    val id: Int = 0,
    val petId: Int,
    val type: TaskType,
    val date: Long,
    val notes: String? = null,
    val reminderTime: Long? = null
)
