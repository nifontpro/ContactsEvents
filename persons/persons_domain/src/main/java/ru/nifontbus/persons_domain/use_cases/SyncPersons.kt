package ru.nifontbus.persons_domain.use_cases

import ru.nifontbus.persons_domain.repository.PersonsRepository

class SyncPersons(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke() = personsRepository.syncPersons()
}