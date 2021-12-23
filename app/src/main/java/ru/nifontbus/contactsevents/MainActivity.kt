package ru.nifontbus.contactsevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import ru.nifontbus.contactsevents.domain.data.EventType
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.presentation.events.EventsScreen
import ru.nifontbus.contactsevents.presentation.events.new_event.NewEventScreen
import ru.nifontbus.contactsevents.presentation.events.new_event.templates.TemplatesScreen
import ru.nifontbus.contactsevents.presentation.events.update.EventUpdateScreen
import ru.nifontbus.contactsevents.presentation.groups.GroupScreen
import ru.nifontbus.contactsevents.presentation.navigation.BottomBar
import ru.nifontbus.contactsevents.presentation.navigation.GetPermission
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.persons.PersonsScreen
import ru.nifontbus.contactsevents.presentation.persons.info.PersonInfoScreen
import ru.nifontbus.contactsevents.ui.theme.ContactsEventsTheme

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactsEventsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GetPermission()
                    val extNavController = rememberNavController()
                    NavHost(
                        navController = extNavController,
                        startDestination = Screen.MainScreen.route
                    ) {
                        composable(Screen.MainScreen.route) {
                            MainScreen(extNavController)
                        }

                        composable(
                            Screen.ExtPersonInfoScreen.route,
                            arguments = listOf(navArgument("id")),
                        ) {
                            PersonInfoScreen(extNavController)
                        }

                        val sharedTemplateState =
                            mutableStateOf(Template(type = EventType.CUSTOM))

                        composable(
                            Screen.ExtNewEventScreen.route,
                            arguments = listOf(navArgument("id")),
                        ) {
                            NewEventScreen(extNavController, sharedTemplateState)
                        }

                        composable(
                            Screen.ExtEventUpdateScreen.route,
                            arguments = listOf(
                                navArgument("person_id"), navArgument("event_id")
                            ),
                        ) {
                            EventUpdateScreen(extNavController, sharedTemplateState)
                        }

                        composable(
                            Screen.ExtTemplatesScreen.route,
                            arguments = listOf(navArgument("id")),
                        ) {
                            TemplatesScreen(extNavController, sharedTemplateState)
                        }
                    }
                }
            }
        }
    }

    private fun navArgument(arg: String) = navArgument(arg) {
        type = NavType.LongType
        defaultValue = -1
        nullable = false
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun MainScreen(extNavController: NavHostController) {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomBar(navController) }
        ) {
            val bottomPadding = it.calculateBottomPadding()
            NavHost(navController = navController, startDestination = Screen.NavEventScreen.route) {

                composable(Screen.NavEventScreen.route) {
                    EventsScreen(extNavController, bottomPadding)
                }
                composable(Screen.NavPersonScreen.route) {
                    PersonsScreen(extNavController, bottomPadding)
                }
                composable(Screen.NavGroupScreen.route) {
                    GroupScreen(extNavController, bottomPadding)
                }
                composable(Screen.NavSettingScreen.route) {
//                    SettingScreen(extNavController, loginViewModel)
                }
            }
        }
    }
}
