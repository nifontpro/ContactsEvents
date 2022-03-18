package ru.nifontbus.settings_domain.use_cases

data class SettingsUseCases(
    val getNotificationState: GetNotificationState,
    val getReposeFeatures: GetReposeFeatures,
    val getAdd40Day: GetAdd40Day,
    val saveNotificationState: SaveNotificationState,
    val saveReposeFeatures: SaveReposeFeatures,
    val saveAdd40Day: SaveAdd40Day,
)
