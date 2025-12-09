package com.lutireh.pettracker.domain.usecases.pet

import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.repository.IPetRepository
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val repository: IPetRepository
) {
    suspend operator fun invoke(petId: Int) = repository.deletePet(petId)
}