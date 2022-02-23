package ru.nifontbus.settings_domain.use_cases

data class MetadataUseCases(
    val sendMessage: SendMessage,
    val subscribeMessage: SubscribeMessage,
    val sendEvent: SendEvent,
    val subscribeEvent: SubscribeEvent
)
