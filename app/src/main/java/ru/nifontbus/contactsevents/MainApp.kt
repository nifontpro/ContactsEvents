package ru.nifontbus.contactsevents

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.nifontbus.worker_domain.util.CHANNEL_ID
import ru.nifontbus.worker_domain.util.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import ru.nifontbus.worker_domain.util.VERBOSE_NOTIFICATION_CHANNEL_NAME
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * Кастомная настройка WorkManager для внедрения зависимостей через Hilt
     */
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    /**
     * Настройка каналов сообщений
     */
    override fun onCreate() {
        super.onCreate()
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
//        } // if
    }
}