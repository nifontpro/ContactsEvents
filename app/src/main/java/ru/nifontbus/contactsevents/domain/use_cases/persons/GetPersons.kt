package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPersons(
    private val contactsRepo: ContactsRepository
) {
    operator fun invoke(): StateFlow<List<Person>> {
        return contactsRepo.persons
    }
}