package ru.nifontbus.groups_domain.use_cases

import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.groups_domain.repository.GroupsRepository

class GetGroupById(private val repository: GroupsRepository) {

    operator fun invoke(id: Long): PersonsGroup? = repository.groups.value.find { group ->
        group.id == id
    }
}