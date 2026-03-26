package com.lutireh.pettracker.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import com.lutireh.pettracker.presentation.pet.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalTasksScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    petViewModel: PetViewModel = hiltViewModel(),
    onAddTask: () -> Unit,
    onEditTask: (PetTaskModel) -> Unit
) {
    val tasks by taskViewModel.allTasks.collectAsState()
    val pets by petViewModel.pets.collectAsState()
    val isLoading by taskViewModel.isLoading.collectAsState()
    val isError by taskViewModel.isError.collectAsState()

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val primaryText = Color(0xFF4A505D)
    val deleteColor = Color(0xFFB4444C)
    
    var selectedTaskForDialog by remember { mutableStateOf<PetTaskModel?>(null) }
    var selectedPetForDialog by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        taskViewModel.getAllTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Minhas Tarefas",
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
                onClick = onAddTask,
                containerColor = primaryColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
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
                isError -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Red)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Erro ao carregar as tarefas.",
                            color = primaryText,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { taskViewModel.getAllTasks() }, colors = ButtonDefaults.buttonColors(containerColor = accentColor)) {
                            Text("Tentar Novamente", color = Color.White)
                        }
                    }
                }
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = accentColor
                    )
                }
                tasks.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.EventBusy, contentDescription = null, modifier = Modifier.size(64.dp), tint = primaryText.copy(alpha = 0.5f))
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Nenhuma tarefa cadastrada",
                            color = primaryText.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(tasks.sortedByDescending { it.date }) { task ->
                            val petName = pets.find { it.id == task.petId }?.name ?: "Pet"
                            GlobalTaskCard(
                                task = task,
                                petName = petName,
                                primaryText = Color(0xFF4A505D),
                                accentColor = accentColor,
                                deleteColor = Color(0xFFB4444C),
                                onClick = { 
                                    selectedTaskForDialog = task
                                    selectedPetForDialog = petName
                                },
                                onDelete = { taskViewModel.deleteTask(it) },
                                onEditTask = onEditTask
                            )
                        }
                    }
                }
            }
        }
    }

    if (selectedTaskForDialog != null) {
        TaskSummaryDialog(
            task = selectedTaskForDialog!!,
            petName = selectedPetForDialog,
            onDismiss = { selectedTaskForDialog = null }
        )
    }
}

@Composable
fun GlobalTaskCard(
    task: PetTaskModel,
    petName: String,
    primaryText: Color,
    accentColor: Color,
    deleteColor: Color,
    onClick: () -> Unit,
    onDelete: (PetTaskModel) -> Unit,
    onEditTask: (PetTaskModel) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault()) }
    
    val icon: ImageVector = when(task.type) {
        TaskType.VACCINE -> Icons.Default.MedicalServices
        TaskType.EXAM -> Icons.Default.Description
        TaskType.BATH -> Icons.Default.Bathtub
        TaskType.VET -> Icons.Default.LocalHospital
        TaskType.WALK -> Icons.Default.DirectionsWalk
        TaskType.FEED -> Icons.Default.Restaurant
        TaskType.OTHER -> Icons.Default.Task
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    text = "${task.type.label} • $petName",
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = dateFormat.format(Date(task.date)),
                    color = primaryText.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = { onEditTask(task) },
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar Tarefa",
                    tint = accentColor
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { onDelete(task) },
                modifier = Modifier
                    .size(40.dp)
                    .background(deleteColor.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Excluir Tarefa",
                    tint = deleteColor
                )
            }
        }
    }
}
