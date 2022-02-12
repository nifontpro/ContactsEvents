package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository

class SaveReposeFeatures(
    private val repository: SettingsRepository
) {
    operator fun invoke(value: Boolean) {
        repository.saveReposeState(value)
    }
}