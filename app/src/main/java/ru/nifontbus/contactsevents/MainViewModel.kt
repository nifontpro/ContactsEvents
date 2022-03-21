package ru.nifontbus.contactsevents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.settings_domain.use_cases.SettingsUseCases
import ru.nifontbus.worker_domain.use_cases.WorkerUseCases
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases,
    workerUseCases: WorkerUseCases
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1)
            _isLoading.value = false
            if (settingsUseCases.getSettings().value.showNotifications) {
                workerUseCases.startWorker()
            }
        }
    }
}