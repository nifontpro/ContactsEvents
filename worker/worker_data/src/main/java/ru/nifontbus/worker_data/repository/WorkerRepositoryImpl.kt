package ru.nifontbus.worker_data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.nifontbus.worker_data.worker.NotificationWorker
import ru.nifontbus.worker_domain.repository.WorkerRepository
import ru.nifontbus.worker_domain.util.WORK_TAG
import java.time.Duration
import javax.inject.Inject

class WorkerRepositoryImpl @Inject constructor(
    context: Context
): WorkerRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun startWorker() {
        Log.e("my", "Init Worker on repository")

//            val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            Duration.ofMinutes(15), // Периодичность
            Duration.ofMinutes(0) // Смещение внутри периода
        )
            .addTag(WORK_TAG)
//            .setInitialDelay(1, TimeUnit.MINUTES) // Начальная задержка
            .build()

        workManager.enqueueUniquePeriodicWork(
            "worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

//            workManager.enqueueUniqueWork("worker", ExistingWorkPolicy.KEEP, myWorkRequest)
    }

    override fun cancelWorks() {
        workManager.cancelAllWorkByTag(WORK_TAG)
    }
}