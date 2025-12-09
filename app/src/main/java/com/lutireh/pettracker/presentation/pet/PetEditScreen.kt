package com.lutireh.pettracker.presentation.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lutireh.pettracker.domain.model.PetModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEditScreen(
    petId: String?,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onError: () -> Unit,
    viewModel: PetDetailsViewModel = hiltViewModel()
) {
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val secondaryColor = Color(0xFF96E1FF)

    val pet by viewModel.selectedPet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(petId) {
        if (petId.isNullOrEmpty()) {
            onError()
        } else {
            viewModel.getPetById(petId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Pet", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        if (isLoading || pet == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = accentColor)
            }
        } else {
            EditPetContent(
                pet = pet!!,
                accentColor = accentColor,
                secondaryColor = secondaryColor,
                onSave = { updatedPet ->
                    viewModel.updatePet(updatedPet)
                    onSaved()
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun EditPetContent(
    pet: PetModel,
    accentColor: Color,
    secondaryColor: Color,
    onSave: (PetModel) -> Unit,
    modifier: Modifier = Modifier
) {
    // States
    var name by remember { mutableStateOf(pet.name) }
    var breed by remember { mutableStateOf(pet.breed ?: "") }
    var age by remember { mutableStateOf(pet.age ?: "") }
    var weight by remember { mutableStateOf(pet.weight ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Foto do pet
        if (pet.photoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(pet.photoUri),
                contentDescription = pet.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { /* Futuro: trocar foto */ },
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(0.2f))
                    .clickable { /* Futuro: trocar foto */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Pets,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Campos do formulário
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                PetInputField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome",
                )

                PetInputField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = "Raça"
                )

                PetInputField(
                    value = age,
                    onValueChange = { age = it },
                    label = "Idade (anos)",
                    shouldShowNumberKeyboard = true
                )


                PetInputField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = "Peso (kg)",
                        shouldShowNumberKeyboard = true
                    )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Botão salvar
        Button(
            onClick = {
                onSave(
                    pet.copy(
                        name = name,
                        breed = breed,
                        age = age,
                        weight = weight
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = secondaryColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Salvar alterações", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    shouldShowNumberKeyboard: Boolean? = false
) {
    val borderColor = Color(0xFFCB954A)
    val textColor = Color(0xFF4A505D)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = textColor) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor.copy(alpha = 0.4f),
            cursorColor = borderColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (shouldShowNumberKeyboard == true) KeyboardType.Number else KeyboardType.Text
        )
    )
}