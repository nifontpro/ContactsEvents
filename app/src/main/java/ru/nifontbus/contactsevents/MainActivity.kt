package ru.nifontbus.contactsevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import ru.nifontbus.contactsevents.presentation.events.EventsScreen
import ru.nifontbus.contactsevents.presentation.groups.GroupScreen
import ru.nifontbus.contactsevents.presentation.navigation.BottomBar
import ru.nifontbus.contactsevents.presentation.navigation.GetPermission
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.persons.PersonsScreen
import ru.nifontbus.contactsevents.ui.theme.ContactsEventsTheme

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
                    MainScreen(extNavController)
                }
            }
        }
    }
}

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
