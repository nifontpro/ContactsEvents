package ru.nifontbus.contactsevents

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.toolbar.ExperimentalToolbarApi
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.presentation.events.EventsScreen
import ru.nifontbus.contactsevents.presentation.events.templates.TemplatesScreen
import ru.nifontbus.contactsevents.presentation.events.update.EventUpdateScreen
import ru.nifontbus.contactsevents.presentation.events.update.NewEventScreen
import ru.nifontbus.contactsevents.presentation.groups.GroupScreen
import ru.nifontbus.contactsevents.presentation.navigation.Arg
import ru.nifontbus.contactsevents.presentation.navigation.BottomBar
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.navigation.permission.GetPermission
import ru.nifontbus.contactsevents.presentation.persons.PersonsScreen
import ru.nifontbus.contactsevents.presentation.persons.info.PersonInfoScreen
import ru.nifontbus.contactsevents.presentation.settings.SettingScreen
import ru.nifontbus.contactsevents.ui.theme.ContactsEventsTheme

@ExperimentalToolbarApi
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
                    GetPermission(
                        Manifest.permission.READ_CONTACTS,
                        stringResource(R.string.sReadDenied)
                    ) {
                        ConfigureExtNavigate()
                    }
                }
            }
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
                        GroupScreen(/*extNavController, */bottomPadding)
                    }
                    composable(Screen.NavSettingScreen.route) {
                        SettingScreen(paddingValues)
                    }
                }
            }
        }
    }
}