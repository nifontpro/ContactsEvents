package ru.nifontbus.core.domain.model

data class Person(
    val displayName: String = "",
    val groups: List<Long> = emptyList(),
    val hasPhoneNumber: Boolean = false,
    val photoUri: String? = null,
    var id: Long = -1,
//    val key: String = "",
)