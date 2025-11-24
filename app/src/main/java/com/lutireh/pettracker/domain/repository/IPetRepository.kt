package com.lutireh.pettracker.domain.repository

import com.lutireh.pettracker.domain.model.PetModel
import kotlinx.coroutines.flow.Flow

interface IPetRepository {

    suspend fun getAllPets(): Flow<List<PetModel>>
    suspend fun getPetById(petId: Int): Flow<PetModel?>
    suspend fun addPet(pet: PetModel)
    suspend fun updatePet(pet: PetModel)
    suspend fun deletePet(pet: PetModel)

}