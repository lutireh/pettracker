package com.lutireh.pettracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_activities")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val type: String,
    val date: Long,
    val notes: String? = null,
    val reminderTime: Long? = null
)
