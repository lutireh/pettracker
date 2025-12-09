package com.lutireh.pettracker.data.repository

import com.lutireh.pettracker.data.local.dao.PetDAO
import com.lutireh.pettracker.data.mapper.toDomain
import com.lutireh.pettracker.data.mapper.toEntity
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.repository.IPetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PetRepositoryImpl(val petDao: PetDAO) : IPetRepository {

    override suspend fun getAllPets() : Flow<List<PetModel>> {
        return petDao.getAllPets().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getPetById(petId: Int) : Flow<PetModel?> {
        return petDao.getPetById(petId).map {
            it?.toDomain()
        }
    }

    override suspend fun addPet(pet: PetModel) = petDao.insertPet(pet.toEntity())

    override suspend fun updatePet(pet: PetModel) = petDao.updatePet(pet.toEntity())

    override suspend fun deletePet(petId: Int) = petDao.deletePet(petId)
}