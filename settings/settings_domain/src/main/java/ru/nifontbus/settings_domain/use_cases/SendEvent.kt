package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.service.MetadataService


// Отправить событие для рассылки
class SendEvent(
    private val service: MetadataService
) {
    suspend operator fun invoke(event: MainEvent) = service.sendEvent(event)
}