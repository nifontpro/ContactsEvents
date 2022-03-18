package ru.nifontbus.persons_domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.model.PersonInfo


interface PersonsRepository {

    val persons: StateFlow<List<Person>>

    suspend fun getPersonInfo(contactId: Long): PersonInfo

    suspend fun getDisplayPhoto(contactId: Long): ImageBitmap?

    fun syncPersons()

    fun silentSync()
}