package com.lutireh.pettracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lutireh.pettracker.data.local.dao.PetDAO
import com.lutireh.pettracker.data.local.dao.PetTaskDAO
import com.lutireh.pettracker.data.local.entity.PetEntity
import com.lutireh.pettracker.data.local.entity.TaskEntity

@Database(entities = [PetEntity::class, TaskEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDAO
    abstract fun petTaskDao(): PetTaskDAO
}