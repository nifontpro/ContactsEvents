package ru.nifontbus.contactsevents.presentation.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ru.nifontbus.contactsevents.presentation.navigation.BottomNavItem
import ru.nifontbus.contactsevents.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun GroupScreen(
    extNavController: NavController,
    bottomPadding: Dp,
) {
    val viewModel: GroupViewModel = hiltViewModel()
    val currentGroup = viewModel.currentGroup.collectAsState().value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    val groups = viewModel.groups.collectAsState(emptyList()).value
    BottomNavItem.GroupItem.badgeCount.value = groups.size

    Scaffold(
        /*floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    extNavController.navigate(Screen.NavNewGroupScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add group")
            }
        },*/
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(bottom = bottomPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            items(groups) { group ->

                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(HalfGray)
                        .clickable {
                            /*extNavController.navigate(
                                Screen.NavGroupEditScreen.createRoute(group.id)
                            )*/
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "[${group.title}]",
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onBackground
                            )
                            Text(
                                group.account,
                                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                                style = MaterialTheme.typography.h6,
                                color = PrimaryDarkColor
                            )
                        }
                        IconButton(
                            onClick = { viewModel.setCurrentGroup(group) },
                        ) {
                            Icon(
                                imageVector = if (group.id == currentGroup?.id) {
                                    Icons.Default.DoneOutline
                                } else {
                                    Icons.Default.RadioButtonUnchecked
                                },
                                contentDescription = "Current group",
                                tint = if (group.id == currentGroup?.id) {
                                    PrimaryDarkColor
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

