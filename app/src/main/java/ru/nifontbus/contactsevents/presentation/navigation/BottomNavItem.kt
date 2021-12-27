package ru.nifontbus.contactsevents.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.RecentActors
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    var badgeCount: MutableState<Int> = mutableStateOf(0)
) {

    object EventItem : BottomNavItem(
        name = "Events",
        route = Screen.NavEventScreen.route,
        icon = Icons.Default.Home
    )

    object PersonItem : BottomNavItem(
        name = "Persons",
        route = Screen.NavPersonScreen.route,
        icon = Icons.Outlined.Group,
    )

    object GroupItem : BottomNavItem(
        name = "Groups",
        route = Screen.NavGroupScreen.route,
        icon = Icons.Outlined.RecentActors,
    )

    object SettingItem : BottomNavItem(
        name = "Account",
        route = Screen.NavSettingScreen.route,
        icon = Icons.Outlined.Settings,
    )
}
