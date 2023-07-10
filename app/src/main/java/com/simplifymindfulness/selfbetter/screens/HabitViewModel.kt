package com.simplifymindfulness.selfbetter.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifymindfulness.selfbetter.room.Habit
import com.simplifymindfulness.selfbetter.room.HabitRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.simplifymindfulness.selfbetter.room.HabitStatus
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    val habits: Flow<List<Habit>> = repository.getAllHabits()

    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insertHabit(habit)
        }
    }

    fun getHabitStatus(habitId: Int, date: LocalDate): Flow<HabitStatus?> {
        return repository.getHabitStatus(habitId, date)
    }

    fun insertOrUpdateHabitStatus(habitStatus: HabitStatus) {
        viewModelScope.launch {
            repository.insertOrUpdateHabitStatus(habitStatus)
        }
    }
}
