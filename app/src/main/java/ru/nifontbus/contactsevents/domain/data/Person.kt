package ru.nifontbus.contactsevents.domain.data

data class Person(
    val displayName: String = "",
    val groups: List<Long> = emptyList(),
    val hasPhoneNumber: Boolean = false,
    var id: Long = -1,
//    val key: String = "",
)