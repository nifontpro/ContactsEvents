package ru.nifontbus.persons_domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.nifontbus.core.domain.model.PersonsGroup
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.repository.PersonsRepository

class GetPersonsFromGroup(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke(group: StateFlow<PersonsGroup?>): Flow<List<Person>> =
        personsRepository.persons.map { list ->
            list.filter { person ->
                person.groups.contains(group.value?.id)
            }/*.sortedBy { it.fio }*/
        }
}