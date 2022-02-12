package ru.nifontbus.persons_domain.use_cases

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPersons(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke(): StateFlow<List<Person>> {
        return personsRepository.persons
    }
}