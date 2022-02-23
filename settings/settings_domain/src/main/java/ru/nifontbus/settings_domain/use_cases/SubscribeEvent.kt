package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.service.MetadataService

/**
 *  Подписка на рассылаемое сообщение
 */
class SubscribeEvent(
    private val service: MetadataService
) {
    suspend operator fun invoke(
        desiredEvent: MainEvent,
        run: suspend () -> Unit
    ) = service.subscribeEvent(
        desiredEvent = desiredEvent,
        run = run
    )
}