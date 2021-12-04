package ru.nifontbus.contactsevents

import org.junit.Test
import ru.nifontbus.contactsevents.domain.utils.toMonthAndDay
import org.junit.Assert.*

class DateTest {

    @Test
    fun testToMonthAndDayFull() {
        val fullDate = "2021-11-12"
        assertEquals(fullDate.toMonthAndDay(), "11-12")
    }

    @Test
    fun testToMonthAndDayShort() {
        val fullDate = "--11-12"
        assertEquals("11-12", fullDate.toMonthAndDay())
    }
}