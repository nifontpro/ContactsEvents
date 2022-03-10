package ru.nifontbus.contactsevents

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.worker_domain.NotificationWorker
import ru.nifontbus.worker_domain.util.WORK_TAG
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(10)
            _isLoading.value = false

            initWorker()
        }
    }

    private fun initWorker() {
//        val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            Duration.ofDays(1), // Периодичность
            Duration.ofMinutes(0) // Смещение внутри периода
        )
            .addTag(WORK_TAG)
            .setInitialDelay(1, TimeUnit.MINUTES) // Начальная задержка
            .build()

//        workManager.enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.KEEP, myWorkRequest)
        workManager.enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}