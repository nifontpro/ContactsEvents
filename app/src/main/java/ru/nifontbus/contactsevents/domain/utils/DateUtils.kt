package ru.nifontbus.contactsevents.domain.utils

import ru.nifontbus.contactsevents.domain.use_cases.events.MIN_DATE
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

fun String.toLocalDate(
    pattern: String = "yyyy-MM-dd",
    locale: Locale = Locale.getDefault()
): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return LocalDate.parse(this, formatter)
}

fun LocalDate.asString(
    pattern: String = "yyyy-MM-dd",
    locale: Locale = Locale.getDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}

fun String.getStringDate(
    initialFormat: String,
    requiredFormat: String,
    locale: Locale = Locale.getDefault()
): String {
    return this.toLocalDate(initialFormat, locale).asString(requiredFormat, locale)
}

fun String.toMonthAndDay(): String {
    return try {
        if (substring(0, 2) == "--" && length == 7) {
            substring(2, 7)
        } else substring(5..9)
    } catch (e: Exception) {
        MIN_DATE
    }
}

fun String.getFullYear(): Int {
    return try {
        val birthDate = this.toLocalDate()
        val year = ChronoUnit.YEARS.between(birthDate, LocalDate.now())
        year.toInt()
    } catch (e: Exception) {
        -1
    }
}

// https://stackoverflow.com/questions/65388653/convert-string-into-localdate-kotlin
// https://www.rrtutors.com/tutorials/How-to-show-date-picker-with-Jetpack-compose
// https://howtoprogram.xyz/2017/02/11/convert-milliseconds-localdatetime-java/
// https://stackoverflow.com/questions/47006254/how-to-get-current-local-date-and-time-in-kotlin