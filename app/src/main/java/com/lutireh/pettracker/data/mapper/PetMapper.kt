package com.lutireh.pettracker.data.mapper

import com.lutireh.pettracker.data.local.entity.PetEntity
import com.lutireh.pettracker.domain.model.PetModel

fun PetEntity.toDomain(): PetModel {
    return PetModel(
        id = id,
        name = name,
        weight = weight.toString(),
        breed = breed,
        age = age.toString(),
        photoUri = photoUri
    )
}

fun PetModel.toEntity(): PetEntity {
    return PetEntity(
        id = id,
        name = name,
        weight = weight?.toLongOrNull(),
        breed = breed,
        age = age?.toIntOrNull(),
        photoUri = photoUri
    )
}