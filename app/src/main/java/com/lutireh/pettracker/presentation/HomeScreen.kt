package com.lutireh.pettracker.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lutireh.pettracker.domain.model.PetModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import com.lutireh.pettracker.presentation.task.TaskSummaryDialog
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tasks by viewModel.upcomingTasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val primaryText = Color(0xFF4A505D)

    var selectedTaskForDialog by remember { mutableStateOf<PetTaskModel?>(null) }
    var selectedPetForDialog by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🐾 Dashboard",
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
                tasks.isEmpty() -> EmptyTasksState(primaryText, accentColor)
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                text = "Próximos Eventos",
                                style = MaterialTheme.typography.titleLarge,
                                color = primaryText,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                        }

                        items(tasks) { event ->
                            EventCard(
                                event = event,
                                onClick = {
                                    selectedTaskForDialog = event.task
                                    selectedPetForDialog = event.petName
                                }
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
fun EmptyTasksState(textColor: Color, iconColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.EventAvailable,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum evento futuro 🗓️",
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "A agenda dos seus pets está livre!",
            color = textColor.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun EventCard(
    event: DashboardTask,
    onClick: () -> Unit
) {
    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val primaryText = Color(0xFF4A505D)

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault()) }
    val task = event.task
    
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
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pet Avatar
            if (event.petAvatar != null) {
                Image(
                    painter = rememberAsyncImagePainter(event.petAvatar),
                    contentDescription = event.petName,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${event.petName} - ${task.type.label}",
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon, 
                        contentDescription = null, 
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = dateFormat.format(Date(task.date)),
                        color = primaryText.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                if (!task.notes.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.notes,
                        color = primaryText.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}