package ru.nifontbus.contactsevents.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.nifontbus.contactsevents.ui.theme.BadgeBackground
import ru.nifontbus.contactsevents.ui.theme.PrimaryDarkColor
import ru.nifontbus.contactsevents.ui.theme.PrimaryLightColor
import ru.nifontbus.contactsevents.ui.theme.TextWhite

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
 /*   val backgroundColor = Color(
        ColorUtils.blendARGB(MaterialTheme.colors.background.toArgb(), 0x000000, 0.2f)
    )*/
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.onSecondary,
        elevation = 5.dp
    ) {
        items.forEach { item ->

//            Здесь recomposition не работает, т.к. это не State объект!!!
//            val selected = item.route == navController.currentDestination?.route

            val selected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                selectedContentColor = PrimaryDarkColor,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        /*if (item.badgeCount.value > 0) {
                            BadgeBox(
                                backgroundColor = BadgeBackground,
                                badgeContent = {
                                    Text(text = item.badgeCount.value.toString(),
                                        color = Color.White
//                                    color = MaterialTheme.colors.onBackground)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }*/
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}