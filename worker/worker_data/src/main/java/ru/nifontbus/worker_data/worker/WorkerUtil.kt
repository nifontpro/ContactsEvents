package ru.nifontbus.worker_data.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import ru.nifontbus.worker_domain.R
import ru.nifontbus.worker_domain.util.CHANNEL_ID
import kotlin.random.Random

fun showNotification(
    title: String,
    message: String,
    context: Context
) {

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_outline_notifications)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(Random.nextInt(), builder.build())
}

suspend fun CoroutineWorker.showNotificationForeground(
    title: String,
    message: String,
    context: Context
) {
    setForeground(
        ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_outline_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        )
    )
}