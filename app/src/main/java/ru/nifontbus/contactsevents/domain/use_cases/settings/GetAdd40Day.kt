package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class GetAdd40Day(
    private val repository: SettingsRepo
) {
    operator fun invoke() = repository.add40Day
}