package ru.nifontbus.settings_domain.service

import ru.nifontbus.settings_domain.model.MainEvent
import kotlinx.coroutines.flow.SharedFlow

/**
 * Репозиторий для совместных данных (сообщений и др. управляющих данных)
 */

interface MetadataService {

    val event: SharedFlow<MainEvent>

    suspend fun sendMessage(msg: String)

    fun subscribeMessage(): SharedFlow<String>

    suspend fun sendEvent(newEvent: MainEvent)

    fun resetSyncTime()
}