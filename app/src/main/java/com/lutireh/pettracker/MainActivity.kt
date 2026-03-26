package com.lutireh.pettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ListAlt
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lutireh.pettracker.presentation.ErrorScreen
import com.lutireh.pettracker.presentation.HomeScreen
import com.lutireh.pettracker.presentation.pet.PetDetailsScreen
import com.lutireh.pettracker.presentation.pet.PetEditScreen
import com.lutireh.pettracker.presentation.pet.PetFormScreen
import com.lutireh.pettracker.presentation.pet.PetListScreen
import com.lutireh.pettracker.presentation.task.GlobalTasksScreen
import com.lutireh.pettracker.presentation.task.ManageTasksScreen
import com.lutireh.pettracker.presentation.task.TaskFormScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val primaryColor = Color(0xFFCB954A)

            Scaffold(
                bottomBar = {
                    if (currentRoute in listOf("home", "pet_list", "global_tasks")) {
                        NavigationBar(
                            containerColor = Color.White,
                            contentColor = primaryColor
                        ) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text("Home") },
                                selected = currentRoute == "home",
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = primaryColor,
                                    indicatorColor = primaryColor
                                ),
                                onClick = {
                                    navController.navigate("home") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Pets, contentDescription = "Meus Pets") },
                                label = { Text("Meus Pets") },
                                selected = currentRoute == "pet_list",
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = primaryColor,
                                    indicatorColor = primaryColor
                                ),
                                onClick = {
                                    navController.navigate("pet_list") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.ListAlt, contentDescription = "Minhas Tarefas") },
                                label = { Text("Tarefas") },
                                selected = currentRoute == "global_tasks",
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = primaryColor,
                                    indicatorColor = primaryColor
                                ),
                                onClick = {
                                    navController.navigate("global_tasks") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen()
                    }
                    composable("pet_list") {
                        PetListScreen(
                            onAddPetClick = { navController.navigate("add_pet") },
                            onAddTaskClick = { navController.navigate("add_task") },
                            onPetClick = { pet ->
                                navController.navigate("pet_details/${pet.id}")
                            }
                        )
                    }
                    composable("add_pet") {
                        PetFormScreen(
                            onPetSaved = {
                                navController.navigate("pet_list") {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = { navController.navigate("error_screen") }
                        )
                    }
                    composable("error_screen") {
                        ErrorScreen(
                            onClickError = { navController.popBackStack() }
                        )
                    }
                    composable("global_tasks") {
                        GlobalTasksScreen(
                            onAddTask = { navController.navigate("add_task") },
                            onEditTask = { task -> navController.navigate("edit_task/${task.id}") }
                        )
                    }
                    composable("add_task") {
                        TaskFormScreen(
                            onTaskSaved = {
                                navController.navigate("pet_list") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onError = { navController.navigate("error_screen") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("pet_details/{petId}") { backStackEntry ->
                        val petId = backStackEntry.arguments?.getString("petId")!!
                        PetDetailsScreen(
                            petId = petId,
                            onBack = { navController.popBackStack() },
                            onEdit = { navController.navigate("edit_pet/$petId") },
                            onManageTasks = { navController.navigate("manage_tasks/$petId") }
                        )
                    }
                    composable("manage_tasks/{petId}") { backStackEntry ->
                        val petId = backStackEntry.arguments?.getString("petId")!!
                        ManageTasksScreen(
                            petId = petId,
                            onBack = { navController.popBackStack() },
                            onEditTask = { task -> navController.navigate("edit_task/${task.id}") }
                        )
                    }
                    composable("edit_task/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")
                        TaskFormScreen(
                            taskId = taskId,
                            onTaskSaved = {
                                navController.popBackStack()
                            },
                            onError = { navController.navigate("error_screen") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("edit_pet/{petId}") { backStackEntry ->
                        val petId = backStackEntry.arguments?.getString("petId")
                        PetEditScreen(
                            petId = petId,
                            onBack = { navController.popBackStack() },
                            onSaved = {
                                navController.navigate("pet_list")
                            },
                            onError = { navController.navigate("error_screen") }
                        )
                    }
                }
            }
        }
    }
}