package com.lutireh.pettracker.domain.usecases.task

import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.repository.IPetTaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: IPetTaskRepository
) {
    suspend operator fun invoke(taskModel: PetTaskModel)= repository.deleteActivity(taskModel)
}