package com.lutireh.pettracker.presentation.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import com.lutireh.pettracker.presentation.task.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsScreen(
    petId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onManageTasks: () -> Unit,
    viewModel: PetDetailsViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val primaryText = Color(0xFF4A505D)
    val secondaryColor = Color(0xFF96E1FF)

    val pet by viewModel.selectedPet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val tasks by taskViewModel.tasksByPet.collectAsState()

    LaunchedEffect(petId) {
        viewModel.getPetById(petId)
        taskViewModel.getTaskByPet(petId.toInt())
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
                containerColor = secondaryColor,
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
                pet != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            PetHeaderSection(
                                pet = pet!!,
                                accentColor = accentColor,
                                primaryText = primaryText
                            )
                            Spacer(Modifier.height(24.dp))
                            
                            // Detalhes Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    DetailItem(label = "Raça", value = pet!!.breed ?: "-", primaryText)
                                    DetailItem(label = "Idade", value = pet!!.age?.let { "$it anos" } ?: "-", primaryText)
                                    DetailItem(label = "Peso", value = pet!!.weight?.let { "$it kg" } ?: "-", primaryText)
                                }
                            }
                            
                            Spacer(Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Agenda",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = primaryText,
                                    fontWeight = FontWeight.Bold,
                                )
                                TextButton(onClick = onManageTasks) {
                                    Text("Gerenciar")
                                }
                            }
                            
                            if (tasks.isEmpty()) {
                                Text(
                                    "Nenhum evento registrado.",
                                    color = primaryText.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        
                        items(tasks.sortedByDescending { it.date }) { task ->
                            TaskCard(task = task, primaryText = primaryText, accentColor = accentColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PetHeaderSection(
    pet: PetModel,
    accentColor: Color,
    primaryText: Color
) {
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
}

@Composable
fun DetailItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.SemiBold, color = color.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
        Text(value, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TaskCard(task: PetTaskModel, primaryText: Color, accentColor: Color) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault()) }
    val isPast = task.date < System.currentTimeMillis()
    
    val icon: ImageVector = when(task.type) {
        TaskType.VACCINE -> Icons.Default.MedicalServices
        TaskType.EXAM -> Icons.Default.Description
        TaskType.BATH -> Icons.Default.Bathtub
        TaskType.VET -> Icons.Default.LocalHospital
        TaskType.WALK -> Icons.Default.DirectionsWalk
        TaskType.FEED -> Icons.Default.Restaurant
        TaskType.OTHER -> Icons.Default.Task
    }
    
    val cardColor = if (isPast) Color.White else Color(0xFFFDF7F1)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.type.label,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = dateFormat.format(Date(task.date)),
                    color = primaryText.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!task.notes.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.notes,
                        color = primaryText.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
