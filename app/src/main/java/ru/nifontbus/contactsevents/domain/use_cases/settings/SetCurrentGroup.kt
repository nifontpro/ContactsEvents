package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.data.ContactsGroup
import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class SetCurrentGroup(
    private val repository: SettingsRepo
) {
    operator fun invoke(contactsGroup: ContactsGroup?) {
        repository.setCurrentGroup(contactsGroup)
    }
}