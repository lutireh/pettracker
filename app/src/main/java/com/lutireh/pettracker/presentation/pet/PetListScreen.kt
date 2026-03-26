package com.lutireh.pettracker.presentation.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lutireh.pettracker.domain.model.PetModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetViewModel = hiltViewModel(),
    onAddPetClick: () -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    onPetClick: (PetModel) -> Unit
) {
    val pets by viewModel.pets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val primaryText = Color(0xFF4A505D)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🐕 Meus Pets",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPetClick,
                containerColor = primaryColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Pet")
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

                pets.isEmpty() -> EmptyPetsState(primaryText, accentColor)
                else -> PetsList(
                    pets = pets,
                    primaryText = primaryText,
                    primaryColor = primaryColor,
                    onPetClick = onPetClick
                )
            }
        }
    }
}

@Composable
fun EmptyPetsState(textColor: Color, iconColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Pets,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum pet cadastrado 🐾",
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Clique no botão '+' para adicionar seu primeiro pet!",
            color = textColor.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PetsList(
    pets: List<PetModel>,
    primaryText: Color,
    primaryColor: Color,
    onPetClick: (PetModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Seus Animais",
                style = MaterialTheme.typography.titleLarge,
                color = primaryText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
        
        items(pets) { pet ->
            PetCard(
                pet = pet,
                primaryText = primaryText,
                primaryColor = primaryColor,
                onClick = { onPetClick(pet) }
            )
        }
    }
}

@Composable
fun PetCard(
    pet: PetModel,
    primaryText: Color,
    primaryColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pet.photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(pet.photoUri),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Pets,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pet.name,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = pet.breed ?: "Sem raça definida",
                    color = primaryText.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!pet.age.isNullOrEmpty() || !pet.weight.isNullOrEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = (pet.age?.let { "${it} anos" } ?: "") + 
                               (if (!pet.age.isNullOrEmpty() && !pet.weight.isNullOrEmpty()) "  •  " else "") + 
                               (pet.weight?.let { "${it} kg" } ?: ""),
                        color = primaryText.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
