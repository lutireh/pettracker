package com.lutireh.pettracker.presentation.task

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lutireh.pettracker.domain.model.PetTaskModel
import com.lutireh.pettracker.domain.model.TaskType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onTaskSaved: () -> Unit = {},
    onError: () -> Unit = {},
) {
    var petId by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val textColor = Color(0xFF4A505D)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🐾 Adicionar Tarefa",
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
                "Preencha os dados da tarefa do seu pet💕",
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            InputField(
                value = petId,
                onValueChange = { petId = it },
                label = "Pet"
            )
            InputField(
                value = type,
                onValueChange = { type = it },
                label = "Tipo de tarefa"
            )
            TimestampInputField(
                onTimestampSelected = { },
                label = "Data"
            )
            InputField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notas"
            )
            TimestampInputField(
                onTimestampSelected = { },
                label = "Hora do lembrete"
            )
            Button(
                onClick = {
                    val task = PetTaskModel(
                        petId = petId,
                        type = TaskType.valueOf(type),
                        date = date,
                        notes = notes,
                        reminderTime = reminderTime
                    )
                    viewModel.addTask(task)
                    if (viewModel.isError.value) {
                        onError()
                    } else {
                        onTaskSaved()
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
                Icon(Icons.Default.Add, contentDescription = "Salvar Tarefa")
                Text("Salvar Tarefa")
            }
        }
    }
}

@Composable
private fun InputField(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimestampInputField(
    label: String = "Data e hora",
    initialTimestamp: Long? = null,
    onTimestampSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTimestamp by remember {
        mutableStateOf(
            initialTimestamp ?: System.currentTimeMillis()
        )
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(selectedTimestamp) {
        dateFormat.format(Date(selectedTimestamp))
    }

    val accentColor = Color(0xFFCB954A)
    val textColor = Color(0xFF4A505D)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = formattedDate,
            onValueChange = { onTimestampSelected },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            enabled = false,
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Selecionar data e hora",
                    tint = accentColor
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = textColor,
                disabledBorderColor = accentColor.copy(alpha = 0.5f),
                disabledLeadingIconColor = accentColor,
                disabledLabelColor = accentColor
            )
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedTimestamp
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val pickedDate = datePickerState.selectedDateMillis
                        if (pickedDate != null) {
                            calendar.timeInMillis = pickedDate

                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                                    calendar.set(Calendar.MINUTE, minute)
                                    val newTimestamp = calendar.timeInMillis
                                    selectedTimestamp = newTimestamp
                                    onTimestampSelected(newTimestamp)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                        showDatePicker = false
                    }) {
                        Text("OK", color = accentColor)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar", color = accentColor)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState, colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = accentColor,
                        selectedDayContentColor = Color.White,
                        todayDateBorderColor = accentColor,
                    )
                )
            }
        }
    }
}
