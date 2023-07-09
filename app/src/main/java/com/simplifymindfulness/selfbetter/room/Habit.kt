package com.simplifymindfulness.selfbetter.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val time: String,
    val days: String
)
