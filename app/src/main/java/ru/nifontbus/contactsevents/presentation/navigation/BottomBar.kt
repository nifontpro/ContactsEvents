package ru.nifontbus.contactsevents.presentation.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import ru.nifontbus.core_ui.component.BottomNavItem

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BottomBar(navController: NavHostController) {

    BottomNavigationBar(
        items = listOf(
            BottomNavItem.EventItem,
            BottomNavItem.PersonItem,
            BottomNavItem.GroupItem,
            BottomNavItem.SettingItem
        ),
        navController = navController,
        onItemClick = {
            navController.navigate(it.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }

                //Avoid multiple copies of the same destination when
                //re selecting the same item
                launchSingleTop = true

                //Restore state when re selecting a previously selected item
                // Если этот параметр включен, то при обновлении состояния
                // viewModel, состояние экрана не изменится при переключении

                restoreState = true

            }
        }
    )
}
