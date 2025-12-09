package com.lutireh.pettracker.presentation.pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.usecases.pet.PetUseCases
import com.lutireh.pettracker.presentation.isNumberInputNotValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val useCases: PetUseCases
) : ViewModel() {

    private val _pets = MutableStateFlow<List<PetModel>>(emptyList())
    val pets: StateFlow<List<PetModel>> = _pets.asStateFlow()

    private val _selectedPet = MutableStateFlow<PetModel?>(null)
    val selectedPet: StateFlow<PetModel?> = _selectedPet.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _isLoading.value = true
            useCases.getAllPets().collect { list ->
                _pets.value = list
                _isLoading.value = false
            }
        }
    }

    fun selectPet(petId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                useCases.getPetById(petId).collect { selectedPet ->
                    //  _selectedPet.value = selectedPet
                }
            } catch (e: Exception) {
                _isError.value = true
            }
            _isLoading.value = false
        }
    }

    fun clearSelectedPet() {
        _selectedPet.value = null
    }

    fun addPet(pet: PetModel) {
        viewModelScope.launch {
            try {
                _isError.value = false
                if (validatePet(pet)) throw IllegalArgumentException()
                useCases.insertPet(pet)
                loadPets()
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    private fun validatePet(pet: PetModel): Boolean = with(pet) {
        return (name.isEmpty() || breed.isNullOrEmpty() || isNumberInputNotValid(age)|| isNumberInputNotValid(weight))
    }

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            try {
                useCases.deletePet(petId)
                loadPets()
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }
}