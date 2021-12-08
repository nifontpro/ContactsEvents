package ru.nifontbus.contactsevents.domain.data

data class Template(
    val id: Long = -1,
    val type: Int = EventType.CUSTOM,
    val name: String = "",
)