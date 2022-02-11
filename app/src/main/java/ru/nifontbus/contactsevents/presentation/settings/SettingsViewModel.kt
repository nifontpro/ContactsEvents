package ru.nifontbus.contactsevents.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nifontbus.settings_domain.use_cases.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val reposeFeatures = settingsUseCases.getReposeFeatures()
    val add40Day = settingsUseCases.getAdd40Day()

    fun setReposeFeatures(value: Boolean) {
        settingsUseCases.saveReposeFeatures(value)
    }

    fun setAdd40Day(value: Boolean) {
        settingsUseCases.saveAdd40Day(value)
    }

}