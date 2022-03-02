package ru.nifontbus.persons_domain.use_cases

data class PersonsUseCases(
    val getPersons: GetPersons,
    val getPersonById: GetPersonById,
    val getPersonByIdFlow: GetPersonByIdFlow,
    val getPersonsFromGroup: GetPersonsFromGroup,
    val getPersonsFilteredFromGroup: GetPersonsFilteredFromGroup,
    val getPersonInfo : GetPersonInfo,
    val getDisplayPhoto: GetDisplayPhoto,
    val syncPersons: SyncPersons,
    val silentSync: SilentSync
)
