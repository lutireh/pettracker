package com.lutireh.pettracker.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.usecases.task.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val useCases: TaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<PetTaskModel>>(emptyList())
    val tasks: StateFlow<List<PetTaskModel>> = _tasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<PetTaskModel?>(null)
    val selectedTask: StateFlow<PetTaskModel?> = _selectedTask.asStateFlow()

    private val _tasksByPet = MutableStateFlow<List<PetTaskModel>>(emptyList())
    val tasksByPet: StateFlow<List<PetTaskModel>> = _tasksByPet.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    fun addTask(task: PetTaskModel) {
        viewModelScope.launch {
            try {
                _isError.value = false
                useCases.addTaskUseCase(task)
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                useCases.getTaskByIdUseCase(id).collect { selectedTask ->
                    _selectedTask.value = selectedTask
                }
            } catch (e: Exception) {
                _isError.value = true
            }
            _isLoading.value = false
        }
    }

    fun getTaskByPet(petId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                useCases.getPetTaskByPetUseCase(petId).collect { task ->
                    _tasksByPet.value = task
                }
            } catch (e: Exception) {
                _isError.value = true
            }
            _isLoading.value = false
        }
    }

    fun deleteTask(task: PetTaskModel) {
        viewModelScope.launch {
            try {
                useCases.deleteTaskUseCase(task)
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }
}