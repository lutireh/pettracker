package com.lutireh.pettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lutireh.pettracker.data.local.entity.PetEntity
import com.lutireh.pettracker.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetTaskDAO {
    @Query("SELECT * FROM pet_activities WHERE petId = :petId ORDER BY date DESC")
    fun getActivitiesByPet(petId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM pet_activities WHERE id = :taskId LIMIT 1")
    fun getActivityById(taskId: Int): Flow<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: TaskEntity)

    @Delete
    suspend fun deleteActivity(activity: TaskEntity)
}