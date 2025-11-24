package com.lutireh.pettracker.data.mapper

import androidx.compose.runtime.remember
import com.lutireh.pettracker.data.local.entity.TaskEntity
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun TaskEntity.toDomain() : PetTaskModel {
    return PetTaskModel(
        id = id,
        petId = petId.toString(),
        type = TaskType.valueOf(type),
        date = date,
        notes = notes,
        reminderTime = reminderTime
    )
}

fun PetTaskModel.toEntity() : TaskEntity {
    return TaskEntity(
        id = id,
        petId = petId.toInt(),
        type = type.name,
        date = date,
        notes = notes,
        reminderTime = reminderTime
    )
}
