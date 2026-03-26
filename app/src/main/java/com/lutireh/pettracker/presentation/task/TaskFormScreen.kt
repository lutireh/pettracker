package com.lutireh.pettracker.presentation.task

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun TaskFormScreen(
    taskId: String? = null,
    viewModel: TaskViewModel = hiltViewModel(),
    petViewModel: PetViewModel = hiltViewModel(),
    onTaskSaved: () -> Unit = {},
    onError: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var petId by remember { mutableStateOf(0) }
    var type by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<Long?>(null) }
    var notes by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val pets by petViewModel.pets.collectAsState()
    var expandedPet by remember { mutableStateOf(false) }
    var selectedPetName by remember { mutableStateOf("") }

    val taskTypes = TaskType.entries.toTypedArray()
    var expandedType by remember { mutableStateOf(false) }
    var selectedTypeLabel by remember { mutableStateOf("") }

    val selectedTask by viewModel.selectedTask.collectAsState()

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.getTaskById(taskId.toInt())
        }
    }

    LaunchedEffect(selectedTask, pets) {
        if (taskId != null && selectedTask != null && pets.isNotEmpty()) {
            val task = selectedTask!!
            petId = task.petId
            val matchedPet = pets.find { it.id == task.petId }
            selectedPetName = matchedPet?.name ?: ""
            type = task.type.name
            selectedTypeLabel = task.type.label
            date = task.date
            if (task.notes != null) notes = task.notes
            reminderTime = task.reminderTime
        }
    }

    val primaryColor = Color(0xFF96E1FF)
    val accentColor = Color(0xFFCB954A)
    val backgroundColor = Color(0xFFF3F3F8)
    val textColor = Color(0xFF4A505D)

    val isEditing = taskId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "🐾 Editar Tarefa" else "🐾 Adicionar Tarefa",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                if (isEditing) "Altere os dados da tarefa do seu pet💕" else "Preencha os dados da tarefa do seu pet💕",
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expandedPet,
                onExpandedChange = { expandedPet = !expandedPet },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                OutlinedTextField(
                    value = selectedPetName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pet") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPet)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedPet,
                    onDismissRequest = { expandedPet = false }
                ) {
                    pets.forEach { pet ->
                        DropdownMenuItem(
                            text = { Text(pet.name) },
                            onClick = {
                                selectedPetName = pet.name
                                petId = pet.id
                                expandedPet = false
                            }
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                OutlinedTextField(
                    value = selectedTypeLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de tarefa") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    taskTypes.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.label)
                            },
                            onClick = {
                                selectedTypeLabel = option.label
                                type = option.name
                                expandedType = false
                            }
                        )
                    }
                }
            }

            TimestampInputField(
                label = "Data",
                initialTimestamp = date,
                key = date,
                onTimestampSelected = { timestamp ->
                    date = timestamp
                }
            )
            InputField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notas"
            )
            TimestampInputField(
                label = "Data e hora do lembrete",
                initialTimestamp = reminderTime,
                key = reminderTime,
                onTimestampSelected = { timestamp ->
                    reminderTime = timestamp
                }
            )
            
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            Button(
                onClick = {
                    val now = System.currentTimeMillis()
                    val selectedDate = date ?: now
                    
                    // Allow 2 minute grace period
                    val isPastDate = selectedDate < (now - 120000L)
                    val isPastReminder = reminderTime != null && reminderTime!! < (now - 120000L)

                    if (isPastDate) {
                        errorMessage = "Coloque uma data e horário futuros para a tarefa."
                        date = null
                    } else if (isPastReminder) {
                        errorMessage = "Coloque uma data e horário futuros para o alarme."
                        reminderTime = null
                    } else if (type.isEmpty() || petId == 0) {
                        errorMessage = "Preencha todos os campos obrigatórios."
                    } else {
                        errorMessage = null
                        val task = PetTaskModel(
                            id = taskId?.toInt() ?: 0,
                            petId = petId,
                            type = TaskType.valueOf(type),
                            date = selectedDate,
                            notes = notes,
                            reminderTime = reminderTime
                        )
                        viewModel.addTask(task)
                        if (viewModel.isError.value) {
                            onError()
                        } else {
                            onTaskSaved()
                        }
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
    key: Long? = null,
    onTimestampSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTimestamp by remember(initialTimestamp, key) {
        mutableStateOf(initialTimestamp ?: System.currentTimeMillis())
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(selectedTimestamp) {
        if (initialTimestamp == null && key == null) {
            dateFormat.format(Date(System.currentTimeMillis()))
        } else {
            dateFormat.format(Date(selectedTimestamp))
        }
    }

    val accentColor = Color(0xFFCB954A)
    val textColor = Color(0xFF4A505D)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        OutlinedTextField(
            label = { Text(label) },
            value = formattedDate,
            onValueChange = {},
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
            val dateState = rememberDatePickerState(
                initialSelectedDateMillis = selectedTimestamp
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val pickedDate = dateState.selectedDateMillis
                        if (pickedDate != null) {
                            val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            utcCalendar.timeInMillis = pickedDate
                            
                            calendar.set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR))
                            calendar.set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH))
                            calendar.set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH))

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
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = dateState)
            }
        }
    }
}
