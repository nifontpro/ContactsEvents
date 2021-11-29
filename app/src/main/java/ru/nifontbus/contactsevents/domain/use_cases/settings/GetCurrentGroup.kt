package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class GetCurrentGroup(
    private val repository: SettingsRepo
) {
    operator fun invoke() = repository.currentGroup
}