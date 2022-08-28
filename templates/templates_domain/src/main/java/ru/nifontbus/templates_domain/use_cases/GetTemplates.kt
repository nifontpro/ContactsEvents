package ru.nifontbus.templates_domain.use_cases

import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.templates_domain.repository.TemplatesRepository


class GetTemplates(
    private val repository: TemplatesRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = repository.getTemplates(settingsRepository.settings.value.reposeFeatures)
}