package ru.nifontbus.contactsevents.domain.use_cases.persons

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.core.domain.model.Person

class GetPersons(
    private val contactsRepo: ContactsRepository
) {
    operator fun invoke(): StateFlow<List<Person>> {
        return contactsRepo.persons
    }
}