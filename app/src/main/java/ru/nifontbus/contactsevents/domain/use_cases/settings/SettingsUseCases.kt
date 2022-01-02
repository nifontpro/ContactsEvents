package ru.nifontbus.contactsevents.domain.use_cases.settings

data class SettingsUseCases(
    val setCurrentGroup: SetCurrentGroup,
    val getCurrentGroup: GetCurrentGroup,
    val getReposeFeatures: GetReposeFeatures,
    val getAdd40Day: GetAdd40Day,
    val saveReposeFeatures: SaveReposeFeatures,
    val saveAdd40Day: SaveAdd40Day
)
