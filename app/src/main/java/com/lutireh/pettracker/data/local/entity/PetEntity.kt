package com.lutireh.pettracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val breed: String?,
    val age: Int?,
    val weight: Long?,
    val photoUri: String?
)
