package ru.nifontbus.core_ui

sealed class Screen(val route: String) {

    object MainScreen : Screen("main_screen")

    // Bottom bar links
    object NavEventScreen : Screen("event_screen")
    object NavPersonScreen : Screen("person_screen")
    object NavGroupScreen : Screen("group_screen")
    object NavSettingScreen : Screen("setting_screen")

    // External
    object ExtPersonInfoScreen : Screen("person_info_screen?${Arg.personId}={${Arg.personId}}") {
        fun createRoute(personId: Long) = "person_info_screen?${Arg.personId}=$personId"
    }

    object ExtNewEventScreen : Screen("new_event_screen?${Arg.personId}={${Arg.personId}}") {
        fun createRoute(personId: Long) = "new_event_screen?${Arg.personId}=$personId"
    }

    object ExtEventUpdateScreen :
        Screen(
            "event_update_screen?${Arg.personId}={${Arg.personId}}?${Arg.eventId}={${Arg.eventId}}"
        ) {
        fun createRoute(personId: Long, eventId: Long) =
            "event_update_screen?${Arg.personId}=$personId?${Arg.eventId}=$eventId"
    }

    object ExtTemplatesScreen : Screen("templates_screen")
}

object Arg {
    const val personId = "person_id"
    const val eventId = "event_id"
    const val template = "template"
}
