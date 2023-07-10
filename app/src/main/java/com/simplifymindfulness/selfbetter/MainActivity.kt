package com.simplifymindfulness.selfbetter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simplifymindfulness.selfbetter.room.AppDatabase
import com.simplifymindfulness.selfbetter.room.HabitRepository
import com.simplifymindfulness.selfbetter.screens.HabitTrackerApp
import com.simplifymindfulness.selfbetter.screens.HabitViewModel
import com.simplifymindfulness.selfbetter.screens.NewHabitScreen
import com.simplifymindfulness.selfbetter.ui.theme.SelfBetterTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SelfBetterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Create an instance of your HabitRepository
                    val habitDao = AppDatabase.getDatabase(this).habitDao()
                    val habitStatusDao = AppDatabase.getDatabase(this).habitStatusDao()
                    val repository = HabitRepository(habitDao,habitStatusDao)

                    // Create an instance of your HabitViewModel
                    val viewModel = HabitViewModel(repository)

                    NavHost(
                        navController = navController,
                        startDestination = Screen.HabitTracker.route
                    ) {
                        composable(Screen.HabitTracker.route) {
                            HabitTrackerApp(viewModel,navController)
                        }
                        composable(Screen.NewHabit.route) {
                            NewHabitScreen(viewModel,navController)
                        }
                    }
                }
            }
        }
    }

}


