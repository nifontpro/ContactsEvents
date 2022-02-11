package ru.nifontbus.persons_domain.use_cases

data class PersonsUseCases(
//    val addPerson: AddPerson,
    val getPersons: GetPersons,
    val getPersonById: GetPersonById,
    val getPersonByIdFlow: GetPersonByIdFlow,
//    val deletePerson: DeletePerson,
    val getPersonsFromGroup: GetPersonsFromGroup,
    val getPersonsFilteredFromGroup: GetPersonsFilteredFromGroup,
//    val getPersonAge: GetPersonAge,
//    val updatePerson: UpdatePerson,
//    val deletePersonWithEvents: DeletePersonWithEvents,
    val getPersonInfo : GetPersonInfo,
    val getPhotoById: GetPhotoById,
    val getDisplayPhoto: GetDisplayPhoto,
)
