package ru.nifontbus.contactsevents.domain.use_cases.persons

data class PersonsUseCases(
//    val addPerson: AddPerson,
    val getPersons: GetPersons,
    val getPersonById: GetPersonById,
//    val deletePerson: DeletePerson,
    val getPersonsFromGroup: GetPersonsFromGroup,
    val getPersonsFilteredFromGroup: GetPersonsFilteredFromGroup,
//    val getPersonAge: GetPersonAge,
//    val updatePerson: UpdatePerson,
//    val deletePersonWithEvents: DeletePersonWithEvents
)
