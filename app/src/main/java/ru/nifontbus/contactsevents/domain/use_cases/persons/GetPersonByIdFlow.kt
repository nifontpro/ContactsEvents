package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.core.domain.model.Person

class GetPersonByIdFlow(
    private val repository: ContactsRepository
) {

    operator fun invoke(id: Long): Flow<Person?> =
        repository.persons.map { persons ->
            persons.find { person -> person.id == id }
        }
}