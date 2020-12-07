package com.example.employees65apps

import com.example.employees65apps.utils.parseNetworkDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.threeten.bp.LocalDate

/**
 * Utils tests
 */
class UtilsTest {
    @Test
    fun parseNetworkDate_UsualDate() {
        val dateUsual = "1990-12-23"
        assertEquals(LocalDate.parse(dateUsual), parseNetworkDate(dateUsual))
    }

    @Test
    fun parseNetworkDate_UnusualDate() {
        val dateUnusual = "23-12-1990"
        assertEquals(LocalDate.parse("1990-12-23"), parseNetworkDate(dateUnusual))
    }

    @Test
    fun parseNetworkDate_EmptyDate() {
        assertNull(parseNetworkDate(""))
    }

    @Test
    fun parseNetworkDate_Null() {
        assertNull(parseNetworkDate(null))
    }
}