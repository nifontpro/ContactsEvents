package ru.nifontbus.contactsevents.domain.use_cases.template

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetTemplates(
    private val repository: ContactsRepository
) {
    operator fun invoke() = repository.getTemplates()
}