package com.lutireh.pettracker.domain.usecases.task

import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.repository.IPetTaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val repository: IPetTaskRepository
) {
    suspend operator fun invoke(): Flow<List<PetTaskModel>> = repository.getAllActivities()
}
