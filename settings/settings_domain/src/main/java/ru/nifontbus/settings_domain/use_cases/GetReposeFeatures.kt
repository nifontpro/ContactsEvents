package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository

class GetReposeFeatures(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.reposeFeatures
}