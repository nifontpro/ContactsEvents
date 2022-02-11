package ru.nifontbus.contactsevents.domain.use_cases.persons

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.core.domain.model.Person

class GetPersonById(
    private val repository: ContactsRepository
) {

    operator fun invoke(id: Long): Person? =
        repository.persons.value.find {
                person -> person.id == id
        }
}