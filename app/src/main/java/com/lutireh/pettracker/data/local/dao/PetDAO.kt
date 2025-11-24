package com.lutireh.pettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lutireh.pettracker.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDAO {

    @Query("SELECT * FROM pet ORDER BY name ASC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pet WHERE id = :petId LIMIT 1")
    fun getPetById(petId: Int): Flow<PetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)
}