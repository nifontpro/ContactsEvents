package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPersonByIdFlow(
    private val repository: ContactsRepository
) {

    operator fun invoke(id: Long): Flow<Person?> =
        repository.persons.map { persons ->
            persons.find { person -> person.id == id }
        }
}