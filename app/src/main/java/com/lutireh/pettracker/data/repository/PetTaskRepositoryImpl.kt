package com.lutireh.pettracker.data.repository

import com.lutireh.pettracker.data.local.dao.PetTaskDAO
import com.lutireh.pettracker.data.mapper.toDomain
import com.lutireh.pettracker.data.mapper.toEntity
import com.lutireh.pettracker.data.mapper.toModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.repository.IPetTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PetTaskRepositoryImpl(val taskDAO: PetTaskDAO) : IPetTaskRepository {

    override suspend fun getActivitiesByPet(petId: Int): Flow<List<PetTaskModel>> =
        taskDAO.getActivitiesByPet(petId).map { list -> list.map { it.toModel() } }

    override suspend fun getActivityById(id: Int): Flow<PetTaskModel?> =
        taskDAO.getActivityById(id).map { selectedTask -> selectedTask.toModel() }

    override suspend fun insertActivity(taskModel: PetTaskModel) =
        taskDAO.insertActivity(taskModel.toEntity())

    override suspend fun deleteActivity(taskModel: PetTaskModel) =
        taskDAO.deleteActivity(taskModel.toEntity())
}