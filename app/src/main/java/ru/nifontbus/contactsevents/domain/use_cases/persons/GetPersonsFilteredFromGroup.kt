package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.core.domain.model.Person
import ru.nifontbus.core.domain.model.PersonsGroup

class GetPersonsFilteredFromGroup(
    private val repository: ContactsRepository
) {
    operator fun invoke(
        group: StateFlow<PersonsGroup?>,
        filter: String
    ): Flow<List<Person>> =
        repository.persons.map { list ->
            list.filter { person ->
                person.groups.contains(group.value?.id) &&
                        person.displayName.contains(filter, ignoreCase = true)
            }/*.sortedBy { it.fio }*/
        }
}