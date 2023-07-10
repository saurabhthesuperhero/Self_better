package com.simplifymindfulness.selfbetter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@Dao
interface HabitStatusDao {
    @Query("SELECT * FROM HabitStatus WHERE habitId = :habitId AND date = :date")
    fun getStatus(habitId: Int, date: LocalDate): Flow<HabitStatus?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStatus(habitStatus: HabitStatus)
}
