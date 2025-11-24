package com.lutireh.pettracker.domain.usecases.task

import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.repository.IPetTaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetTaskByPetUseCase @Inject constructor(
    private val repository: IPetTaskRepository
) {
    suspend operator fun invoke(petId: Int): Flow<List<PetTaskModel>> = repository.getActivitiesByPet(petId)
}