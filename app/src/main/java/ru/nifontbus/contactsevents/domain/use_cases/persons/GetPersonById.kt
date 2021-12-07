package ru.nifontbus.contactsevents.domain.use_cases.persons

import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPersonById(
    private val repository: ContactsRepository
) {

    operator fun invoke(id: Long): Person? =
        repository.persons.value.find {
                person -> person.id == id
        }
}