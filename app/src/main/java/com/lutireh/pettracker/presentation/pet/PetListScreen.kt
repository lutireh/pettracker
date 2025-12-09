package com.luiza.pettracker.presentation.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Remove
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
import com.lutireh.pettracker.presentation.pet.PetViewModel

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
                onClick = onAddTaskClick,
                containerColor = primaryColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Adicionar Tarefa")
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

                pets.isEmpty() -> EmptyPetsState(onAddPetClick)
                else -> PetsList(
                    pets = pets,
                    onDelete = { viewModel.deletePet(it.id) },
                    onAddPetClick = onAddPetClick,
                    onPetClick = onPetClick
                )
            }
        }
    }
}

@Composable
fun EmptyPetsState(
    onAddPetClick: () -> Unit = {}
) {
    val iconColor = Color(0xFFB4444C)
    val textColor = Color(0xFF4A505D)

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
            text = "Nenhum pet cadastrado ainda 🐾",
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adicione o primeiro clicando no botão abaixo!",
            color = textColor.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddPetClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = iconColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text("Adicionar Pet")
        }
    }
}

@Composable
fun PetsList(
    pets: List<PetModel>,
    onDelete: (PetModel) -> Unit,
    onAddPetClick: () -> Unit,
    onPetClick: (PetModel) -> Unit
) {
    val iconColor = Color(0xFFB4444C)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCCED2))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(12.dp)
        ) {
            Button(
                onClick = onAddPetClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = iconColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Adicionar Pet")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
        ) {
            items(pets.size) { index ->
                val pet = pets[index]
                PetCard(
                    pet = pet,
                    onDelete= onDelete,
                    onClick = { onPetClick(pet) })
            }
        }
    }
}

@Composable
fun PetCard(
    pet: PetModel,
    onDelete: (PetModel) -> Unit,
    onClick: () -> Unit
) {
    val cardColor = Color(0xFFFFF6F1)
    val accentColor = Color(0xFFCB954A)
    val deleteColor = Color(0xFFB4444C)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Pets,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    pet.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A505D),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pet.breed ?: "Sem raça definida",
                    color = Color(0xFF6E5223),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!pet.age.isNullOrEmpty() || !pet.weight.isNullOrEmpty()) {
                    Text(
                        text = "${pet.age} anos  •  ${pet.weight} kg",
                        color = Color(0xFF4A505D).copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                onClick = { onDelete(pet) },
                modifier = Modifier
                    .size(40.dp)
                    .background(deleteColor.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Excluir pet",
                    tint = deleteColor
                )
            }
        }
    }
}
