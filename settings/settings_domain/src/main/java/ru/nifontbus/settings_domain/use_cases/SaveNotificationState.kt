package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository

class SaveNotificationState(
    private val repository: SettingsRepository
) {
    operator fun invoke(value: Boolean) {
        repository.saveNotificationState(value)
    }
}