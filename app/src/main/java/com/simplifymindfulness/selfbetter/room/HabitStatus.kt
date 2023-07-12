package com.simplifymindfulness.selfbetter.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(primaryKeys = ["habitId", "date"])
data class HabitStatus(
    val habitId: Int,
    val date: LocalDate,
    val isDone: Boolean
)

