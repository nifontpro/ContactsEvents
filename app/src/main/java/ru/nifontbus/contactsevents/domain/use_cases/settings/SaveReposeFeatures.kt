package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class SaveReposeFeatures(
    private val repository: SettingsRepo
) {
    operator fun invoke(value: Boolean) {
        repository.saveReposeState(value)
    }
}