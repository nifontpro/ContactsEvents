package ru.nifontbus.settings_presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.use_cases.MetadataUseCases
import ru.nifontbus.settings_domain.use_cases.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases,
    private val metadataUseCases: MetadataUseCases
) : ViewModel() {

    val reposeFeatures = settingsUseCases.getReposeFeatures()
    val add40Day = settingsUseCases.getAdd40Day()

    fun setReposeFeatures(value: Boolean) {
        settingsUseCases.saveReposeFeatures(value)
    }

    fun setAdd40Day(value: Boolean) {
        settingsUseCases.saveAdd40Day(value)
    }

    fun syncAll() = viewModelScope.launch {
        metadataUseCases.sendEvent(MainEvent.SyncAll)
    }
}