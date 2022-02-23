package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.service.MetadataService

// Подписка на рассылаемое сообщение
class SubscribeMessage(
    private val service: MetadataService
) {
    operator fun invoke() = service.subscribeMessage()
}