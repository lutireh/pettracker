package com.lutireh.pettracker.domain.usecases.pet

import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.repository.IPetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetIdUseCase @Inject constructor(
    private val repository: IPetRepository
) {
    suspend operator fun invoke(petId: Int): Flow<PetModel?> = repository.getPetById(petId)
}