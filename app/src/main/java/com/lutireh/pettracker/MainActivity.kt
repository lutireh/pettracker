package com.lutireh.pettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luiza.pettracker.presentation.pet.PetFormScreen
import com.luiza.pettracker.presentation.pet.PetListScreen
import com.lutireh.pettracker.presentation.ErrorScreen
import com.lutireh.pettracker.presentation.pet.PetDetailsScreen
import com.lutireh.pettracker.presentation.pet.PetEditScreen
import com.lutireh.pettracker.presentation.task.TaskFormScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "pet_list") {
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
                composable("add_task") {
                    TaskFormScreen(
                        onTaskSaved = {
                            navController.navigate("pet_list") {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                        },
                        onError = { navController.navigate("error_screen") }
                    )
                }
                composable("pet_details/{petId}") { backStackEntry ->
                    val petId = backStackEntry.arguments?.getString("petId")!!
                    PetDetailsScreen(
                        petId = petId,
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate("edit_pet/$petId") }
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