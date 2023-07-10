package com.simplifymindfulness.selfbetter.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(tableName = "HabitStatus")
data class HabitStatus(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val habitId: Int,
    val date: LocalDate,
    val isDone: Boolean
)
