package ru.nifontbus.groups_domain.use_cases

import ru.nifontbus.groups_domain.repository.GroupsRepository

class SyncGroups(
    private val groupsRepository: GroupsRepository
) {
    operator fun invoke() = groupsRepository.syncGroups()
}