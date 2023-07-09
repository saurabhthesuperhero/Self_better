package com.simplifymindfulness.selfbetter.room

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAll()
    }

    suspend fun insertHabit(habit: Habit) {
        habitDao.insert(habit)
    }
}
