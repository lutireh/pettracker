package com.lutireh.pettracker.domain.model

data class PetModel(
    val id: Int = 0,
    val name: String,
    val breed: String?,
    val age: String?,
    val weight: String?,
    val photoUri: String?
)
