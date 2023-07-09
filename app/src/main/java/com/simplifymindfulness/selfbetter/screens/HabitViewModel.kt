package com.simplifymindfulness.selfbetter.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifymindfulness.selfbetter.room.Habit
import com.simplifymindfulness.selfbetter.room.HabitRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    val habits: LiveData<List<Habit>> = repository.getAllHabits().asLiveData()

    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insertHabit(habit)
        }
    }
}
