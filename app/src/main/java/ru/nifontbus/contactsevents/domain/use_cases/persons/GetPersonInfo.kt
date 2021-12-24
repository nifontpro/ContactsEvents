package ru.nifontbus.contactsevents.domain.use_cases.persons

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPersonInfo(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(id: Long) = repository.getPersonInfo(id)
}