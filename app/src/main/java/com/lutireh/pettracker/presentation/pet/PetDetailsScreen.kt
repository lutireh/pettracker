package com.lutireh.pettracker.presentation.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lutireh.pettracker.domain.model.PetModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsScreen(
    petId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: PetDetailsViewModel = hiltViewModel()
) {
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val primaryText = Color(0xFF4A505D)

    val pet by viewModel.selectedPet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(petId) {
        viewModel.getPetById(petId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Pet", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEdit,
                containerColor = accentColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Pet")
            }
        },
        containerColor = backgroundColor
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = accentColor
                    )
                }

                pet != null -> PetDetailsContent(
                    pet = pet!!,
                    accentColor = accentColor,
                    primaryText = primaryText
                )
            }
        }
    }
}

@Composable
fun PetDetailsContent(
    pet: PetModel,
    accentColor: Color,
    primaryText: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Foto + Nome
        if (pet.photoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(pet.photoUri),
                contentDescription = pet.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = accentColor, modifier = Modifier.size(64.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            pet.name,
            style = MaterialTheme.typography.headlineMedium,
            color = primaryText,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Card de detalhes
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                DetailItem(label = "Raça", value = pet.breed ?: "Sem raça definida", primaryText)
                DetailItem(label = "Idade", value = pet.age ?: "Não informada", primaryText)
                DetailItem(label = "Peso", value = pet.weight ?: "Não informado", primaryText)
            }
        }
    }
}


@Composable
fun DetailItem(label: String, value: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, color = color)
        Text(value, color = color.copy(alpha = 0.8f))
    }
}
