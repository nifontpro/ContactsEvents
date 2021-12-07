package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.data.PersonsGroup
import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class SetCurrentGroup(
    private val repository: SettingsRepo
) {
    operator fun invoke(personsGroup: PersonsGroup?) {
        repository.setCurrentGroup(personsGroup)
    }
}