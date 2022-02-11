package ru.nifontbus.contactsevents.domain.use_cases.template

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.settings_domain.repository.SettingsRepository

class GetTemplates(
    private val repository: ContactsRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = repository.getTemplates(settingsRepository.reposeFeatures.value)
}