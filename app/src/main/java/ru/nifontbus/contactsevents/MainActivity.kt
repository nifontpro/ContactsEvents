package ru.nifontbus.contactsevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.presentation.events.EventsScreen
import ru.nifontbus.contactsevents.presentation.events.templates.TemplatesScreen
import ru.nifontbus.contactsevents.presentation.events.update.EventUpdateScreen
import ru.nifontbus.contactsevents.presentation.events.update.NewEventScreen
import ru.nifontbus.contactsevents.presentation.groups.GroupScreen
import ru.nifontbus.contactsevents.presentation.navigation.Arg
import ru.nifontbus.contactsevents.presentation.navigation.BottomBar
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.persons.PersonsScreen
import ru.nifontbus.contactsevents.presentation.persons.info.PersonInfoScreen
import ru.nifontbus.contactsevents.presentation.settings.SettingScreen
import ru.nifontbus.contactsevents.ui.theme.ContactsEventsTheme

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            ContactsEventsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    GetReadContactsPermission()
                }
            }
        }
    }

    @ExperimentalPermissionsApi
    @Composable
    private fun GetReadContactsPermission() {
        val readContact = rememberPermissionState(android.Manifest.permission.READ_CONTACTS)
        PermissionRequired(
            permissionState = readContact,
            permissionNotGrantedContent = {
                LaunchedEffect(true) {
                    readContact.launchPermissionRequest()
                }
            },
            permissionNotAvailableContent = {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        "Read contacts permission denied. See this FAQ with information about why we " +
                                "need this permission. Please, grant us access on the Settings screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {}) {
                        Text("Open Settings")
                    }
                }
            }
        ) {
            ConfigureExtNavigate()
        }
    }

    @Composable
    private fun ConfigureExtNavigate() {
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
                arguments = listOf(navArgument(Arg.personId)),
            ) {
                PersonInfoScreen(extNavController)
            }

            composable(
                Screen.ExtNewEventScreen.route,
                arguments = listOf(navArgument(Arg.personId)),
            ) { entry ->
                val returnTemplate =
                    entry.arguments?.getParcelable<Template>(Arg.template)
                NewEventScreen(extNavController, returnTemplate)
            }

            composable(
                Screen.ExtEventUpdateScreen.route,
                arguments = listOf(
                    navArgument(Arg.personId), navArgument(Arg.eventId)
                ),
            ) { entry ->
                val returnTemplate =
                    entry.arguments?.getParcelable<Template>(Arg.template)
                EventUpdateScreen(extNavController, returnTemplate)
            }

            composable(
                Screen.ExtTemplatesScreen.route,
            ) {
                TemplatesScreen(extNavController)
            }
        }
    }

    private fun navArgument(arg: String) = navArgument(arg) {
        type = NavType.LongType
        defaultValue = -1
        nullable = false
    }

    @Composable
    private fun MainScreen(extNavController: NavHostController) {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomBar(navController) }
            ) { paddingValues ->
                val bottomPadding = paddingValues.calculateBottomPadding()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NavEventScreen.route
                ) {

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
                        SettingScreen(paddingValues)
                    }
                }
            }
        }
    }
}