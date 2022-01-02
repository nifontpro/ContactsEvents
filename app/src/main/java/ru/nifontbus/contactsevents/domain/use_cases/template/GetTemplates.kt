package ru.nifontbus.contactsevents.domain.use_cases.template

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class GetTemplates(
    private val repository: ContactsRepository,
    private val settingsRepo: SettingsRepo
) {
    operator fun invoke() = repository.getTemplates(settingsRepo.reposeFeatures.value)
}