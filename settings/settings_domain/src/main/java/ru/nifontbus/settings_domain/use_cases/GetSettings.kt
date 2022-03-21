package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository

class GetSettings(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.settings
}