package ru.nifontbus.groups_domain.use_cases

import ru.nifontbus.groups_domain.repository.GroupsRepository

class GetCurrentGroup(
    private val repository: GroupsRepository
) {
    operator fun invoke() = repository.currentGroup
}