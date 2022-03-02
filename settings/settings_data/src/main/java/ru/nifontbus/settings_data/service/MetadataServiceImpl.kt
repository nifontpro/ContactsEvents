package ru.nifontbus.settings_data.service

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.service.MetadataService

/**
 * Репозиторий для совместных управляющих данных,
 * не относящихся к предметной области приложения
 */

class MetadataServiceImpl(
    context: Context
) : MetadataService {

    private val _message = MutableSharedFlow<String>()
    private val message = _message.asSharedFlow()

    private val _event = MutableSharedFlow<MainEvent>(0)
    override val event = _event.asSharedFlow()

    private var lastUpdateTime: Long = 0

    init {
        context.contentResolver.registerContentObserver(
            ContactsContract.Data.CONTENT_URI, false,
            MyObserver(Handler(Looper.getMainLooper()))
        )
    }

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
     * Сброс таймера синхронизации
     */
    override fun resetSyncTime() {
        lastUpdateTime = System.currentTimeMillis()
    }

    /**
     * Класс-наблюдатель за изменениями в данных контактов
     */
    inner class MyObserver(handler: Handler?) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange)
            Log.e("my", "Sync data, uri = $uri")
            val time = System.currentTimeMillis()
            // Проверка минимального времени между синхронизациями (30с.)
            if (time - lastUpdateTime > 1000 * 30) {
                lastUpdateTime = time
                CoroutineScope(Dispatchers.Default).launch {
//                    sendEvent(MainEvent.SilentSyncAll)
                    _event.emit(MainEvent.SilentSyncAll)
                    Log.e("my", "Real sync data")
                }
            }
        }
    }
}