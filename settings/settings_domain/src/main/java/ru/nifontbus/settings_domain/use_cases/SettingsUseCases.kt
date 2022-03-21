package ru.nifontbus.settings_domain.use_cases

data class SettingsUseCases(
    val saveNotificationState: SaveNotificationState,
    val saveReposeFeatures: SaveReposeFeatures,
    val saveAdd40Day: SaveAdd40Day,
    val getSettings: GetSettings
)
