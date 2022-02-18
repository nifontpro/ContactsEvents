package ru.nifontbus.groups_domain.use_cases

import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.groups_domain.repository.GroupsRepository

class SetCurrentGroup(
    private val repository: GroupsRepository
) {
    operator fun invoke(personsGroup: PersonsGroup?) {
        repository.setCurrentGroup(personsGroup)
    }
}