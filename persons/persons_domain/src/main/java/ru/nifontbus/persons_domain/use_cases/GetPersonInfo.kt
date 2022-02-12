package ru.nifontbus.persons_domain.use_cases

import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPersonInfo(
    private val personsRepository: PersonsRepository
) {
    suspend operator fun invoke(id: Long) = personsRepository.getPersonInfo(id)
}