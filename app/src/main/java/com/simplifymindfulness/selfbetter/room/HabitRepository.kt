package com.simplifymindfulness.selfbetter.room

import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class HabitRepository(private val habitDao: HabitDao, private val habitStatusDao: HabitStatusDao) {
    fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAll()
    }

    suspend fun insertHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    fun getHabitStatus(habitId: Int, date: LocalDate): Flow<HabitStatus?> {
        return habitStatusDao.getStatus(habitId, date)
    }

    suspend fun insertOrUpdateHabitStatus(habitStatus: HabitStatus) {
        habitStatusDao.insertOrUpdateStatus(habitStatus)
    }
}
