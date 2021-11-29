package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.data.ContactsGroup
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPersonsFromGroup(
    private val repository: ContactsRepository
) {
    operator fun invoke(group: StateFlow<ContactsGroup?>): Flow<List<Person>> {
        return repository.persons.map { list ->
            list.filter { person ->
                person.groups.contains(group.value?.id)
            }/*.sortedBy { it.fio }*/
        }
    }
}