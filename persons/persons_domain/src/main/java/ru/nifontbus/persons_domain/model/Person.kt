package ru.nifontbus.persons_domain.model

import androidx.compose.ui.graphics.ImageBitmap

data class Person(
    val displayName: String = "",
    val groups: List<Long> = emptyList(),
    val photo: ImageBitmap? = null,
    var id: Long = -1,
    val lookup: String = ""
)