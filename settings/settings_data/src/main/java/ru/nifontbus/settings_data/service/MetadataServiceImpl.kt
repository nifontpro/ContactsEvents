package ru.nifontbus.settings_data.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.service.MetadataService

/**
 * Репозиторий для совместных управляющих данных,
 * не относящихся к предметной области приложения
 */

class MetadataServiceImpl : MetadataService {

    private val _message = MutableSharedFlow<String>()
    private val message = _message.asSharedFlow()

    private val _event = MutableSharedFlow<MainEvent>()
    private val event = _event.asSharedFlow()


    /**
     * Рассылает сообщение всем подписчикам
     */
    override suspend fun sendMessage(msg: String) {
        _message.emit(msg)
    }

    override fun subscribeMessage(): SharedFlow<String> = message

    /**
     * Рассылает событие всем подписчикам
     */
    override suspend fun sendEvent(newEvent: MainEvent) {
        _event.emit(newEvent)
    }

    /**
     * Подписка на нужное событие
     */
    override suspend fun subscribeEvent(
        desiredEvent: MainEvent,
        run: suspend () -> Unit
    ) {
        event.collectLatest {
            if (it == desiredEvent) {
                run()
            }
        }
    }
}