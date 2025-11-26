package com.lutireh.pettracker.data.mapper

import com.lutireh.pettracker.data.local.entity.TaskEntity
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType


fun PetTaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        petId = petId,
        type = type.name,
        date = date,
        notes = notes,
        reminderTime = reminderTime
    )
}

fun TaskEntity.toModel(): PetTaskModel {
    return PetTaskModel(
        id = id,
        petId = petId,
        type = TaskType.valueOf(type),
        date = date,
        notes = notes,
        reminderTime = reminderTime
    )
}
