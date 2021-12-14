package ru.nifontbus.contactsevents.domain.use_cases.persons

import android.net.Uri
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetPhotoByUri(
    private val repository: ContactsRepository
) {
    operator fun invoke(photoUri: String) = repository.getPhotoByUri(photoUri)
}