package com.lutireh.pettracker.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskSummaryDialog(
    task: PetTaskModel,
    petName: String,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault()) }
    val primaryText = Color(0xFF4A505D)
    val accentColor = Color(0xFFCB954A)

    val icon: ImageVector = when(task.type) {
        TaskType.VACCINE -> Icons.Default.MedicalServices
        TaskType.EXAM -> Icons.Default.Description
        TaskType.BATH -> Icons.Default.Bathtub
        TaskType.VET -> Icons.Default.LocalHospital
        TaskType.WALK -> Icons.Default.DirectionsWalk
        TaskType.FEED -> Icons.Default.Restaurant
        TaskType.OTHER -> Icons.Default.Task
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Detalhes da Tarefa",
                    color = primaryText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        text = {
            Column {
                HorizontalDivider(color = accentColor.copy(alpha = 0.2f), modifier = Modifier.padding(bottom = 12.dp))
                
                DetailRow(Icons.Default.Pets, "Pet", petName, primaryText, accentColor)
                DetailRow(Icons.Default.Category, "Tipo", task.type.label, primaryText, accentColor)
                DetailRow(Icons.Default.Event, "Data", dateFormat.format(Date(task.date)), primaryText, accentColor)
                
                if (task.reminderTime != null) {
                    DetailRow(Icons.Default.NotificationsActive, "Alarme", dateFormat.format(Date(task.reminderTime)), primaryText, accentColor)
                }
                
                if (!task.notes.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Notas adicionais:", fontWeight = FontWeight.SemiBold, color = primaryText, style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        color = Color(0xFFF3F3F8),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = task.notes,
                            modifier = Modifier.padding(12.dp),
                            color = primaryText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = accentColor, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String, textColor: Color, accentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = accentColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, color = textColor, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, color = textColor, style = MaterialTheme.typography.bodyMedium)
    }
}
