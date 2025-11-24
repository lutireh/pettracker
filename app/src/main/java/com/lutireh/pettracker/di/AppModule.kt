package com.lutireh.pettracker.di

import android.content.Context
import androidx.room.Room
import com.lutireh.pettracker.data.local.AppDatabase
import com.lutireh.pettracker.data.local.dao.PetDAO
import com.lutireh.pettracker.data.local.dao.PetTaskDAO
import com.lutireh.pettracker.data.repository.PetRepositoryImpl
import com.lutireh.pettracker.data.repository.PetTaskRepositoryImpl
import com.lutireh.pettracker.domain.repository.IPetRepository
import com.lutireh.pettracker.domain.repository.IPetTaskRepository
import com.lutireh.pettracker.domain.usecases.pet.AddPetUseCase
import com.lutireh.pettracker.domain.usecases.pet.DeletePetUseCase
import com.lutireh.pettracker.domain.usecases.pet.GetAllPetsUseCase
import com.lutireh.pettracker.domain.usecases.pet.GetPetIdUseCase
import com.lutireh.pettracker.domain.usecases.pet.PetUseCases
import com.lutireh.pettracker.domain.usecases.pet.UpdatePetUseCase
import com.lutireh.pettracker.domain.usecases.task.AddTaskUseCase
import com.lutireh.pettracker.domain.usecases.task.DeleteTaskUseCase
import com.lutireh.pettracker.domain.usecases.task.GetPetTaskByPetUseCase
import com.lutireh.pettracker.domain.usecases.task.GetTaskByIdUseCase
import com.lutireh.pettracker.domain.usecases.task.TaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pet_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePetDao(db: AppDatabase): PetDAO = db.petDao()

    @Provides
    fun providePetActivityDao(db: AppDatabase): PetTaskDAO = db.petTaskDao()

    @Provides
    @Singleton
    fun providePetRepository(dao: PetDAO): IPetRepository =
        PetRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePetActivityRepository(dao: PetTaskDAO): IPetTaskRepository =
        PetTaskRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePetUseCases(repository: IPetRepository): PetUseCases =
        PetUseCases(
            getAllPets = GetAllPetsUseCase(repository),
            getPetById = GetPetIdUseCase(repository),
            insertPet = AddPetUseCase(repository),
            deletePet = DeletePetUseCase(repository),
            updatePetUseCase = UpdatePetUseCase(repository)
        )

    @Provides
    @Singleton
    fun providePetActivityUseCases(repository: IPetTaskRepository): TaskUseCase =
        TaskUseCase(
            getPetTaskByPetUseCase = GetPetTaskByPetUseCase(repository),
            getTaskByIdUseCase = GetTaskByIdUseCase(repository),
            addTaskUseCase = AddTaskUseCase(repository),
            deleteTaskUseCase = DeleteTaskUseCase(repository)
        )
}