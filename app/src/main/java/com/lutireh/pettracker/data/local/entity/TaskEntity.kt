package com.lutireh.pettracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity(tableName = "pet_activities")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val type: String,
    val date: String?,
    val notes: String?,
    val reminderTime: String?
)