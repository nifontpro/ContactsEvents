package ru.nifontbus.contactsevents.domain.data

data class Person(
    val displayName: String = "",
    val groups: List<Int> = emptyList(),
    val hasPhoneNumber: Boolean = false,
    var id: Int = -1,
//    val key: String = "",
)