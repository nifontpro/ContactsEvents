package ru.nifontbus.core_ui

sealed class Screen(val route: String) {

    object MainScreen : Screen("main_screen")

    // Bottom bar links
    object NavEventScreen : Screen("event_screen")
    object NavPersonScreen : Screen("person_screen")
    object NavGroupScreen : Screen("group_screen")
    object NavSettingScreen : Screen("setting_screen")

    // External
    object ExtPersonInfoScreen : Screen("person_info_screen?${Argument.personId}={${Argument.personId}}") {
        fun createRoute(personId: Long) = "person_info_screen?${Argument.personId}=$personId"
    }

    object ExtNewEventScreen : Screen("new_event_screen?${Argument.personId}={${Argument.personId}}") {
        fun createRoute(personId: Long) = "new_event_screen?${Argument.personId}=$personId"
    }

    object ExtEventUpdateScreen :
        Screen(
            "event_update_screen?${Argument.personId}={${Argument.personId}}?${Argument.eventId}={${Argument.eventId}}"
        ) {
        fun createRoute(personId: Long, eventId: Long) =
            "event_update_screen?${Argument.personId}=$personId?${Argument.eventId}=$eventId"
    }

    object ExtTemplatesScreen : Screen("templates_screen")
}

object Argument {
    const val personId = "person_id"
    const val eventId = "event_id"
    const val template = "template"
}
