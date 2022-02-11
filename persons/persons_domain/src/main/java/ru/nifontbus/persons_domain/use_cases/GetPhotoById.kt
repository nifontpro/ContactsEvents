package ru.nifontbus.persons_domain.use_cases

import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPhotoById(
    private val personsRepository: PersonsRepository
) {
    suspend operator fun invoke(id: Long) = personsRepository.getPhotoById(id)
}