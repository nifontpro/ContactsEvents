package ru.nifontbus.groups_domain.use_cases

data class GroupsUseCases(
    val getGroups: GetGroups,
    val getGroupById: GetGroupById,
    val setCurrentGroup: SetCurrentGroup,
    val getCurrentGroup: GetCurrentGroup,
)
