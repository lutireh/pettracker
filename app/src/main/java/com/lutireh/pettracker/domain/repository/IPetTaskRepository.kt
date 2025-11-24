package com.lutireh.pettracker.domain.repository

import com.lutireh.pettracker.domain.model.PetTaskModel
import kotlinx.coroutines.flow.Flow

interface IPetTaskRepository {

    suspend fun getActivitiesByPet(petId: Int): Flow<List<PetTaskModel>>
    suspend fun getActivityById(id: Int) : Flow<PetTaskModel?>
    suspend fun insertActivity(taskModel: PetTaskModel)
    suspend fun deleteActivity(taskModel: PetTaskModel)
}