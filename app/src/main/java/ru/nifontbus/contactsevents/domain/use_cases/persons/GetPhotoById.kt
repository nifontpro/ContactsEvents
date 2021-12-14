package ru.nifontbus.contactsevents.domain.use_cases.persons

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPhotoById(
    private val repository: ContactsRepository
) {
    operator fun invoke(id: Long) = repository.getPhotoById(id)
}