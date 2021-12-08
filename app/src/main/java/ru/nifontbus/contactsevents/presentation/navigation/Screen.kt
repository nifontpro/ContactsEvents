package ru.nifontbus.contactsevents.presentation.navigation

sealed class Screen(val route: String) {

    object MainScreen: Screen("main_screen")

    // Bottom bar links
    object NavEventScreen: Screen("event_screen")
    object NavPersonScreen: Screen("person_screen")
    object NavGroupScreen: Screen("group_screen")
    object NavSettingScreen: Screen("setting_screen")

    // External
    object NavNewPersonScreen: Screen("new_person_screen")
    object NavPersonInfoScreen: Screen("person_info_screen?id={id}") {
        fun createRoute(id: Long) = "person_info_screen?id=$id"
    }
    object NavNewEventScreen: Screen("new_event_screen?id={id}") {
        fun createRoute(id: Long) = "new_event_screen?id=$id"
    }

    object NavTemplatesScreen: Screen("templates_screen")

    object NavPersonInfoMainScreen: Screen("person_info_main_screen")
    object NavPersonEditScreen: Screen("person_edit_screen")

    object NavNewGroupScreen: Screen("new_group_screen")
    object NavGroupEditScreen: Screen("group_edit_screen?id={id}") {
        fun createRoute(id: String) = "group_edit_screen?id=$id"
    }

    object NavLoginScreen: Screen("login_screen")
    object NavNewEventMainScreen: Screen("new_event_main_screen")
    object NavNewTemplateScreen: Screen("new_template_screen")

}
