package ru.nifontbus.persons_domain.use_cases

import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPersonById(
    private val personsRepository: PersonsRepository
) {

    operator fun invoke(id: Long): Person? =
        personsRepository.persons.value.find {
                person -> person.id == id
        }
}