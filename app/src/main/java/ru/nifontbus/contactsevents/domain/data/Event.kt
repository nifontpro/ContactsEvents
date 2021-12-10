package ru.nifontbus.contactsevents.domain.data

import android.content.Context
import android.provider.ContactsContract
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.utils.asString
import ru.nifontbus.contactsevents.domain.utils.toLocalDate
import ru.nifontbus.contactsevents.domain.utils.toMonthAndDay
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Event(
    val label: String = "",
    val date: String = "",
    val type: Int = EventType.CUSTOM,
    val personId: Long = -1,
    val id: Long = -1
) {

    fun getDescription(context: Context) =
        when (type) {
            EventType.CUSTOM -> label
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }

    fun getFullYear(): Int? {
        return try {
            val birthDate = date.toLocalDate()
            val year = ChronoUnit.YEARS.between(birthDate, LocalDate.now())
            year.toInt()
        } catch (e: Exception) {
            null
        }
    }

    fun daysLeft(): Long {
        return try {
            val now = LocalDate.now()
            val nowString = now.asString()
            val nowMd = nowString.toMonthAndDay()
            val dateMd = date.toMonthAndDay()
            val yearEvent =
                if (dateMd >= nowMd) {
                    now.year
                } else {
                    now.year + 1
                }
            val fullDate = "$yearEvent-$dateMd".toLocalDate()
            ChronoUnit.DAYS.between(now, fullDate)
        } catch (e: Exception) {
            -1
        }
    }
}