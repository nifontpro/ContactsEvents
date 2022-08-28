package ru.nifontbus.settings_domain.repository

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val showNotifications: Boolean = true,
    val reposeFeatures: Boolean = true,
    val add40Day: Boolean = true,
)