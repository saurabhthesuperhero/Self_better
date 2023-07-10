package com.simplifymindfulness.selfbetter.room

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

object Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(daysSinceEpoch: Long): LocalDate {
        return LocalDate.ofEpochDay(daysSinceEpoch)
    }
}
