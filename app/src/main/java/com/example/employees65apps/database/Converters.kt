package com.example.employees65apps.database

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

/**
 * Converters providing storing [LocalDate] in database
 */
object Converters {
    @JvmStatic
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(it)
        }
    }

    @JvmStatic
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }
}