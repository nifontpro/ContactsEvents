package ru.nifontbus.groups_domain.use_cases

import ru.nifontbus.groups_domain.repository.GroupsRepository

class GetGroups(
    private val repository: GroupsRepository
) {
    operator fun invoke() = repository.groups
}