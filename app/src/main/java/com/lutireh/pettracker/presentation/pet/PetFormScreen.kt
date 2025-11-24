package com.luiza.pettracker.presentation.pet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.presentation.pet.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    viewModel: PetViewModel = hiltViewModel(),
    onPetSaved: () -> Unit = {},
    onError: () -> Unit = {},
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val textColor = Color(0xFF4A505D)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🐾 Adicionar Pet",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Preencha os dados do seu pet 💕",
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            PetInputField(
                value = name,
                onValueChange = { name = it },
                label = "Nome"
            )
            PetInputField(
                value = breed,
                onValueChange = { breed = it },
                label = "Raça"
            )
            PetInputField(
                value = age,
                onValueChange = { age = it },
                label = "Idade (anos)"
            )
            PetInputField(
                value = weight,
                onValueChange = { weight = it },
                label = "Peso (kg)"
            )
            Button(
                onClick = {
                    val pet = PetModel(
                        name = name,
                        breed = breed,
                        age = age,
                        weight = weight,
                        photoUri = null
                    )
                    viewModel.addPet(pet)
                    if (viewModel.isError.value) {
                        onError()
                    } else {
                        onPetSaved()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Salvar Pet")
                Text("Salvar Pet")
            }
        }
    }
}

@Composable
private fun PetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
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
        )
    )
}