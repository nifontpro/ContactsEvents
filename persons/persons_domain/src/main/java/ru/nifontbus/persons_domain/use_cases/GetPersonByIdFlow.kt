package ru.nifontbus.persons_domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPersonByIdFlow(
    private val personsRepository: PersonsRepository
) {

    operator fun invoke(id: Long): Flow<Person?> =
        personsRepository.persons.map { persons ->
            persons.find { person -> person.id == id }
        }
}