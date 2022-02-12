package ru.nifontbus.contactsevents.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.nifontbus.core_ui.BadgeBackground
import ru.nifontbus.core_ui.PrimaryDarkColor
import ru.nifontbus.core_ui.component.BottomNavItem

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

            val lineLength = animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 300
                )
            )

            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                selectedContentColor = PrimaryDarkColor,
                unselectedContentColor = Color.Gray,
                icon = {
//                    BottomItem(item, selected)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .drawBehind {
                                if (lineLength.value > 0f) {
                                    drawLine(
                                        color = if (selected) PrimaryDarkColor
                                        else Color.Gray,
                                        start = Offset(
                                            size.width / 2f - lineLength.value * 15.dp.toPx(),
                                            size.height
                                        ),
                                        end = Offset(
                                            size.width / 2f + lineLength.value * 15.dp.toPx(),
                                            size.height
                                        ),
                                        strokeWidth = 2.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }


                            }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )

                        if (item.badgeCount.value > 0) {
                            /*val alertText = if (item.badgeCount.value > 999) {
                                "999+"
                            } else {
                                item.badgeCount.value.toString()
                            }*/
                            Text(
                                text = " ${item.badgeCount.value} ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BadgeBackground)
                            )
                        }
                    }
                }
            )
        }
    }
}