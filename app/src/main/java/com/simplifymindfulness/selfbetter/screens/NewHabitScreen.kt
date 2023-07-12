package com.simplifymindfulness.selfbetter.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simplifymindfulness.selfbetter.Screen
import com.simplifymindfulness.selfbetter.room.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHabitScreen(viewModel: HabitViewModel, navController: NavController) {
    var selectedTime by remember { mutableStateOf("Anytime") }
    var habitName by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Everyday") }
    var selectedDays by remember { mutableStateOf(listOf<String>()) } // Change this to a list
    var showDays by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary) // Set the background color
                .fillMaxWidth() // Make the background color fill the width
        ) {
            Text(
                text = "Create New Habit",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ), // Set the font weight to bold and the color to white
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Use a larger typography style for the main heading
            Spacer(modifier = Modifier.height(16.dp)) // Add some spacing
            Text("What Do You name it ?", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp)) // Add some spacing
            OutlinedTextField(
                value = habitName,
                onValueChange = {
                    habitName = it
                    if (it.isNotBlank()) {
                        errorMessage = null // Clear the error message when the user types something
                    }
                },
                label = { Text("Habit Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                "When do you want to do this habit?",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp)) // Add some spacing
            LazyRow {
                items(listOf("Anytime", "Morning", "Afternoon", "Evening", "Night")) { time ->
                    if (time == selectedTime) {
                        Button(
                            onClick = { selectedTime = time },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(time)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { selectedTime = time },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(time)
                        }
                    }
                }
            }

            Text("Habit Days", style = MaterialTheme.typography.headlineSmall)
            Row {
                listOf("Everyday", "Customize").forEach { option ->
                    if (option == selectedOption) {
                        Button(
                            onClick = {
                                selectedOption = option
                                showDays = option == "Customize" // Update showDays here
                            },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(option)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                selectedOption = option
                                showDays = option == "Customize" // Update showDays here
                            },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(option)
                        }
                    }
                }
            }

            if (showDays) {
                Log.e("TAG", "NewHabitDialog:showDays  ${showDays}")
                LazyRow {
                    items(listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) { day ->
                        if (selectedDays.contains(day)) { // Check if the day is in the selectedDays list
                            Button(
                                onClick = {
                                    selectedDays = selectedDays - day
                                }, // Remove the day from the list when clicked
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                            ) {
                                Text(day)
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    selectedDays = selectedDays + day
                                }, // Add the day to the list when clicked
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                            ) {
                                Text(day)
                            }
                        }
                    }
                }
            } else {
                selectedDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            }
            Spacer(modifier = Modifier.weight(1f)) // This pushes the remaining content to the top

            Button(
                onClick = {
                    if (habitName.isBlank()) {
                        errorMessage = "Habit name cannot be empty"
                    } else {
                        val newHabit = Habit(
                            id = 0,  // The ID will be auto-generated by Room
                            name = habitName,
                            time = selectedTime,
                            days = selectedDays.joinToString(",")  // Convert the list of days to a comma-separated string
                        )

                        // Use the ViewModel to insert the new Habit into the database
                        viewModel.insertHabit(newHabit)
                        navController.navigate(Screen.HabitTracker.route) {
                            popUpTo(Screen.NewHabit.route) { inclusive = true }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth() // This makes the button stretch fully
                    .padding(16.dp) // Add some padding
            ) {
                Text("Save")
            }
        }

    }

}