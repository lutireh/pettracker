package com.lutireh.pettracker.presentation.pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.usecases.pet.PetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val useCases: PetUseCases
) : ViewModel() {

    private val _selectedPet = MutableStateFlow<PetModel?>(null)
    val selectedPet: StateFlow<PetModel?> = _selectedPet.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()


    fun getPetById(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                useCases.getPetById(id.toInt()).collect {
                    _selectedPet.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun deletePet(id: String) {
        viewModelScope.launch {
            useCases.deletePet(id.toInt())
        }
    }


    fun updatePet(pet: PetModel) {
        viewModelScope.launch {
            try {
                useCases.updatePetUseCase(pet)
//                loadPets()
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }
}