package com.lutireh.pettracker.presentation.pet

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PetDetailsScreen(
    petId: Int,
    viewModel: PetViewModel = hiltViewModel()
) {
    val pet by viewModel.selectedPet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(petId) {
        viewModel.selectPet(petId)
    }

    when {
        isLoading -> CircularProgressIndicator()
        pet != null -> PetDetailsContent(pet = pet!!)
        else -> Text("Pet não encontrado 😿")
    }
}