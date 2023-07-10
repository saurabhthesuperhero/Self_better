package com.simplifymindfulness.selfbetter.screens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simplifymindfulness.selfbetter.Screen
import com.simplifymindfulness.selfbetter.room.HabitStatus
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

@Composable
fun HabitTrackerApp(viewModel: HabitViewModel, navController: NavController) {

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarView()
        TaskListView(viewModel, LocalDate.now())  // Pass the current date here
        Button(
            onClick = { navController.navigate(Screen.NewHabit.route) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Create New Habit")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarView() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()
    Log.e("TAG", "CalendarView: $today")

    val weeks = getWeeksFromToday(today, 52)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        weeks.size
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        val displayText = when {
            selectedDate == today -> "Today"
            else -> selectedDate.toString()
        }
        Text(
            text = displayText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            val weekDates = weeks[page]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)  // Add horizontal padding
            ) {
                weekDates.forEach { date ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)  // Add a fixed height
                            .clip(CircleShape)
                            .clickable(
                            ) { selectedDate = date }
                            .background(if (date == selectedDate) MaterialTheme.colorScheme.onTertiary else Color.Transparent),

                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

fun getWeeksFromToday(today: LocalDate, weeksCount: Int): List<List<LocalDate>> {
    val weeks = mutableListOf<List<LocalDate>>()
    var currentStartOfWeek = today
    while (currentStartOfWeek.dayOfWeek != DayOfWeek.SUNDAY) {
        currentStartOfWeek = currentStartOfWeek.minusDays(1)
    }
    repeat(weeksCount) {
        val week = (0 until 7).map { currentStartOfWeek.plusDays(it.toLong()) }
        weeks.add(week)
        currentStartOfWeek = currentStartOfWeek.plusWeeks(1)
    }
    weeks.forEach { week ->
        Log.e("TAG", "Week: ${week.joinToString(", ")}")
    }
    return weeks
}


@Composable
fun TaskListView(viewModel: HabitViewModel, date: LocalDate) {  // Add the ViewModel as a parameter
    val habits by viewModel.habits.collectAsState(initial = emptyList())  // Use collectAsState instead of observeAsState

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)  // This makes the LazyColumn take up only 70% of the available space
    ) {
        items(habits) { habit ->  // Use the habits list here
            val status by viewModel.getHabitStatus(habit.id, date).collectAsState(null)
            var isTicked by remember { mutableStateOf(status?.isDone ?: false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(if (isTicked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        isTicked = !isTicked
                        viewModel.insertOrUpdateHabitStatus(HabitStatus(0, habit.id, date, isTicked))
                    }) {
                        Icon(
                            imageVector = if (isTicked) Icons.Default.Check else Icons.Default.CheckCircle,
                            contentDescription = if (isTicked) "Checked" else "Unchecked",
                            tint = if (isTicked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = habit.name,  // Display the habit name
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}




