package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.data.PersonsGroup
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

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