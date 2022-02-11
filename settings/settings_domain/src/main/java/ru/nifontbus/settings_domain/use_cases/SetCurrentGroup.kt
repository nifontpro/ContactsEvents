package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.core.domain.model.PersonsGroup
import ru.nifontbus.settings_domain.repository.SettingsRepository

class SetCurrentGroup(
    private val repository: SettingsRepository
) {
    operator fun invoke(personsGroup: PersonsGroup?) {
        repository.setCurrentGroup(personsGroup)
    }
}