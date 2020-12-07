package com.example.employees65apps.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException

/**
 * Parse String date in formats yyyy-MM-dd or dd-MM-yyyy to LocalDate, otherwise return null
 * @param date String date
 * @return LocalDate or null
 */
fun parseNetworkDate(date: String?): LocalDate? {
    if (date.isNullOrEmpty())
        return null

    return try {
        LocalDate.parse(date)
    } catch (e: DateTimeParseException) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        } catch (e: DateTimeParseException) {
            null
        }
    }
}