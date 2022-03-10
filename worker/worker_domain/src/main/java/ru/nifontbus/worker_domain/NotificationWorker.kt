package ru.nifontbus.worker_domain

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import ru.nifontbus.worker_domain.repository.WorkerRepository
import ru.nifontbus.worker_domain.util.showNotification

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: WorkerRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.e("my", "Work Manager: start")

        return try {

//            val taskDataString = inputData.getString(MESSAGE_STATUS)
//            showNotificationForeground(taskDataString.toString(), context)
//            showNotification(taskDataString.toString(), context)
            val events = repository.getEventsNow()
            events.forEach {
                showNotification(
                    title = it.displayName,
                    message = it.getDescription(context),
                    context = context
                )
                delay(2000L)
            }
            Result.success()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Result.failure()
        }
    }
}

/*
Inject worker:
https://developer.android.com/training/dependency-injection/hilt-jetpack
 */