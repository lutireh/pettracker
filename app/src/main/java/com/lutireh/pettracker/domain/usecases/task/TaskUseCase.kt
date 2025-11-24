package com.lutireh.pettracker.domain.usecases.task

data class TaskUseCase(
    val addTaskUseCase: AddTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val getPetTaskByPetUseCase: GetPetTaskByPetUseCase
)
