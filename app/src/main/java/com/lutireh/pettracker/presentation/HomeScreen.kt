//package com.lutireh.pettracker.presentation
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import com.luiza.pettracker.presentation.pet.EmptyPetsState
//import com.luiza.pettracker.presentation.pet.PetsList
//import com.lutireh.pettracker.presentation.pet.PetViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    viewModel: HomeViewModel
//) {
//    val nextTasks // by viewModel.nextTasks.collectAsState()
//
//    val primaryColor = Color(0xFF96E1FF)
//    val accentColor = Color(0xFFCB954A)
//    val backgroundColor = Color(0xFFF3F3F8)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Pet Tracker",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = accentColor
//                )
//            )
//        },
//        containerColor = backgroundColor
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//        ) {
//            when {
//                isLoading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center),
//                        color = accentColor
//                    )
//                }
//                nextTasks.isEmpty() -> EmptyTasksState()
//                else -> TasksList(
//                    tasks = tasks,
//                    onDelete = { viewModel.deletePet(it) }
//                )
//            }
//        }
//
//    }
//}