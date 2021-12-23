package ru.nifontbus.contactsevents.presentation.navigation

sealed class Screen(val route: String) {

    object MainScreen : Screen("main_screen")

    // Bottom bar links
    object NavEventScreen : Screen("event_screen")
    object NavPersonScreen : Screen("person_screen")
    object NavGroupScreen : Screen("group_screen")
    object NavSettingScreen : Screen("setting_screen")

    // External
    object ExtPersonInfoScreen : Screen("person_info_screen?id={id}") {
        fun createRoute(id: Long) = "person_info_screen?id=$id"
    }

    object ExtNewEventScreen : Screen("new_event_screen?id={id}") {
        fun createRoute(id: Long) = "new_event_screen?id=$id"
    }

    object ExtEventUpdateScreen :
        Screen("event_update_screen?person_id={person_id}?event_id={event_id}") {
        fun createRoute(personId: Long, eventId: Long) =
            "event_update_screen?person_id=$personId?event_id=$eventId"
    }

    object ExtTemplatesScreen : Screen("templates_screen")
}
