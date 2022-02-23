package ru.nifontbus.settings_domain.use_cases

data class SettingsUseCases(
    val getReposeFeatures: GetReposeFeatures,
    val getAdd40Day: GetAdd40Day,
    val saveReposeFeatures: SaveReposeFeatures,
    val saveAdd40Day: SaveAdd40Day,
    val sendMessage: SendMessage,
    val subscribeMessage: SubscribeMessage,
    val sendEvent: SendEvent
)
