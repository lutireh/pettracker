package com.lutireh.pettracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.usecases.pet.PetUseCases
import com.lutireh.pettracker.domain.usecases.task.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardTask(
    val task: PetTaskModel,
    val petName: String,
    val petAvatar: String?
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petUseCases: PetUseCases,
    private val taskUseCases: TaskUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _upcomingTasks = MutableStateFlow<List<DashboardTask>>(emptyList())
    val upcomingTasks: StateFlow<List<DashboardTask>> = _upcomingTasks.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                petUseCases.getAllPets(),
                taskUseCases.getAllTasksUseCase()
            ) { pets, tasks ->
                val petMap = pets.associateBy { it.id }
                // Use a slightly past time to avoid missing things from today if just happened a bit ago
                // Let's just show all tasks that haven't occurred more than 24 hours ago
                val cutoffTime = System.currentTimeMillis() - 86400000L
                
                tasks.filter { it.date >= cutoffTime }
                    .sortedBy { it.date }
                    .mapNotNull { task ->
                        val pet = petMap[task.petId] ?: return@mapNotNull null
                        DashboardTask(
                            task = task,
                            petName = pet.name,
                            petAvatar = pet.photoUri
                        )
                    }
            }.collect { dashboardList ->
                _upcomingTasks.value = dashboardList
                _isLoading.value = false
            }
        }
    }
}