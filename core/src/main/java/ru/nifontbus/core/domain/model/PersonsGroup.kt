package ru.nifontbus.core.domain.model

import android.content.Context
import ru.nifontbus.core.R

data class PersonsGroup(
    val title: String = "",
    val account: String = "",
    val id: Long = -1,
) {
    fun localTitle(context: Context): String =
    when (title) {
        "My Contacts" -> context.getString(R.string.group_my_contacts)
        "Starred in Android" -> context.getString(R.string.group_starred_in_android)
        "Friends" -> context.getString(R.string.group_friends)
        "Family" -> context.getString(R.string.group_family)
        "Coworkers" -> context.getString(R.string.group_coworkers)
        else -> title
    }
}