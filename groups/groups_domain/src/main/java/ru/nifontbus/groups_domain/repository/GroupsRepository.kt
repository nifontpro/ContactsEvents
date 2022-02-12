package ru.nifontbus.groups_domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.groups_domain.model.PersonsGroup

interface GroupsRepository {

    val groups: StateFlow<List<PersonsGroup>>

}