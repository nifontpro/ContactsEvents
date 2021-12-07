package ru.nifontbus.contactsevents.domain.use_cases.groups

import ru.nifontbus.contactsevents.domain.data.PersonsGroup
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetGroupById(private val repository: ContactsRepository) {

    operator fun invoke(id: Long): PersonsGroup? = repository.groups.value.find { group ->
        group.id == id
    }
}