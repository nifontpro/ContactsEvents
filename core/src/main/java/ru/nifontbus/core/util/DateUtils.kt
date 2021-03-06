package ru.nifontbus.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

const val MIN_DATE = "01-01"

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

/*
fun String.getStringDate(
    initialFormat: String,
    requiredFormat: String,
    locale: Locale = Locale.getDefault()
): String {
    return this.toLocalDate(initialFormat, locale).asString(requiredFormat, locale)
}
*/

fun String.toMonthAndDay(): String {
    return try {
        if (substring(0, 2) == "--" && length == 7) {
            substring(2, 7)
        } else substring(5..9)
    } catch (e: Exception) {
        MIN_DATE
    }
}

fun String.isShortDate() = length == 7

fun String.toShortDate(): String {
    return "--" + substring(5, 10)
}

fun String.getLocalizedDate(): String {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    var short = false
    val str = if (substring(0, 2) == "--" && length == 7) {
        short = true
        "0001${substring(1, 7)}"
    } else this

    val date = str.toLocalDate()
    val dateStr = date.format(dateFormatter)
    val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())
    return if (!short) dateStr else {
        val dateStrShort = date.format(formatter)
        dateStrShort.substring(0, 5)
    }
}

// https://stackoverflow.com/questions/65388653/convert-string-into-localdate-kotlin
// https://www.rrtutors.com/tutorials/How-to-show-date-picker-with-Jetpack-compose
// https://howtoprogram.xyz/2017/02/11/convert-milliseconds-localdatetime-java/
// https://stackoverflow.com/questions/47006254/how-to-get-current-local-date-and-time-in-kotlin