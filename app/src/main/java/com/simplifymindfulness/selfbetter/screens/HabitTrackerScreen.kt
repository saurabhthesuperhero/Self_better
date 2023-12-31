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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import com.simplifymindfulness.selfbetter.Screen
import com.simplifymindfulness.selfbetter.room.HabitStatus
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HabitTrackerApp(viewModel: HabitViewModel, navController: NavController) {
    var selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarView(selectedDate = selectedDate)
        TaskListView(viewModel, selectedDate)
//        ColorShadesView(MaterialTheme.colorScheme)

        Spacer(modifier = Modifier.weight(1f)) // This pushes the remaining content to the top
// Pass the current date here
        Button(
            onClick = { navController.navigate(Screen.NewHabit.route) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth() // This makes the button stretch fully
                .padding(16.dp)
        ) {
            Text("Create New Habit")
        }

    }
}

@Composable
fun ColorShadesView(colorScheme: ColorScheme) {
    val colorProperties = mapOf(
        "Primary" to colorScheme.primary,
        "OnPrimary" to colorScheme.onPrimary,
        "PrimaryContainer" to colorScheme.primaryContainer,
        "OnPrimaryContainer" to colorScheme.onPrimaryContainer,
        "InversePrimary" to colorScheme.inversePrimary,
        "Secondary" to colorScheme.secondary,
        "OnSecondary" to colorScheme.onSecondary,
        "SecondaryContainer" to colorScheme.secondaryContainer,
        "OnSecondaryContainer" to colorScheme.onSecondaryContainer,
        "Tertiary" to colorScheme.tertiary,
        "OnTertiary" to colorScheme.onTertiary,
        "TertiaryContainer" to colorScheme.tertiaryContainer,
        "OnTertiaryContainer" to colorScheme.onTertiaryContainer,
        "Background" to colorScheme.background,
        "OnBackground" to colorScheme.onBackground,
        "Surface" to colorScheme.surface,
        "OnSurface" to colorScheme.onSurface,
        "SurfaceVariant" to colorScheme.surfaceVariant,
        "OnSurfaceVariant" to colorScheme.onSurfaceVariant,
        "SurfaceTint" to colorScheme.surfaceTint,
        "InverseSurface" to colorScheme.inverseSurface,
        "InverseOnSurface" to colorScheme.inverseOnSurface,
        "Error" to colorScheme.error,
        "OnError" to colorScheme.onError,
        "ErrorContainer" to colorScheme.errorContainer,
        "OnErrorContainer" to colorScheme.onErrorContainer,
        "Outline" to colorScheme.outline,
        "OutlineVariant" to colorScheme.outlineVariant,
        "Scrim" to colorScheme.scrim
    )

    LazyColumn {
        items(colorProperties.entries.toList()) { (propertyName, colorValue) ->
            colorValue?.let { color ->
                ColorShadeItem(propertyName, color)
            }
        }
    }
}



@Composable
fun ColorShadeItem(name: String, color: Color) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color)
    ) {
        val contentColor = contentColorFor(color)
        Text(
            text = name,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarView(selectedDate: MutableState<LocalDate>) {
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
            selectedDate.value == today -> "Today"
            selectedDate.value == today.plusDays(1) -> "Tomorrow"
            else -> {
                val dateFormat = if (selectedDate.value.year == today.year) {
                    DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())
                } else {
                    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
                }
                selectedDate.value.format(dateFormat)
            }
        }

        Text(
            text = displayText,
            style = MaterialTheme.typography.headlineLarge.copy( fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        val weekName = selectedDate.value.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        Text(
            text = weekName,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
            modifier = Modifier.padding(start = 20.dp, bottom = 16.dp)
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
                            ) { selectedDate.value = date }
                            .background(if (date == selectedDate.value) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),

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
fun TaskListView(
    viewModel: HabitViewModel,
    date: MutableState<LocalDate>
) {  // Add the ViewModel as a parameter
    val habits by viewModel.habits.collectAsState(initial = emptyList())  // Use collectAsState instead of observeAsState
    val filteredHabits = habits.filter { habit ->
        val habitDays = habit.days.split(",")  // Assuming days is a comma-separated string
        habitDays.contains(date.value.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)  // This makes the LazyColumn take up only 70% of the available space
    ) {
        items(filteredHabits) { habit ->  // Use the habits list here
            val status by viewModel.getHabitStatus(habit.id, date.value).collectAsState(null)
            var isTicked by remember { mutableStateOf(status?.isDone ?: false) }

            // Update isTicked whenever status changes
            LaunchedEffect(status) {
                isTicked = status?.isDone ?: false
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
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
                        viewModel.insertOrUpdateHabitStatus(
                            HabitStatus(
                                habit.id,
                                date.value,
                                isTicked
                            )
                        )  // Always use 0 as the id here

                    }) {
                        Icon(
                            imageVector = if (isTicked) Icons.Default.CheckCircle else Icons.Default.CheckCircle,
                            contentDescription = if (isTicked) "Checked" else "Unchecked",
                            tint = if (isTicked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = habit.name,  // Display the habit name
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,

                    )
                }
            }
        }
    }
}








