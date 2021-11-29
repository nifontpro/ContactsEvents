package ru.nifontbus.contactsevents.domain.use_cases.groups

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetGroups(
    private val repository: ContactsRepository
) {
    operator fun invoke() = repository.groups
}