package ru.nifontbus.contactsevents.domain.use_cases.settings

import ru.nifontbus.contactsevents.domain.repository.SettingsRepo

class SaveAdd40Day(
    private val repository: SettingsRepo
) {
    operator fun invoke(value: Boolean) {
        repository.saveAdd40DayState(value)
    }
}