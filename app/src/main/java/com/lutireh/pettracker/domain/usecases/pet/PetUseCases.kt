package com.lutireh.pettracker.domain.usecases.pet

data class PetUseCases(
    val getAllPets: GetAllPetsUseCase,
    val getPetById: GetPetIdUseCase,
    val insertPet: AddPetUseCase,
    val deletePet: DeletePetUseCase,
    val updatePetUseCase: UpdatePetUseCase
)
