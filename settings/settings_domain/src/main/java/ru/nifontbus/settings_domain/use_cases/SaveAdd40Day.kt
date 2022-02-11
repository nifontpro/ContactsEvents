package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository

class SaveAdd40Day(
    private val repository: SettingsRepository
) {
    operator fun invoke(value: Boolean) {
        repository.saveAdd40DayState(value)
    }
}