package ru.nifontbus.contactsevents.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.ui.theme.DeepBlue
import ru.nifontbus.contactsevents.ui.theme.HalfGray
import ru.nifontbus.contactsevents.ui.theme.LightRed
import ru.nifontbus.contactsevents.ui.theme.PrimaryDarkColor

@ExperimentalMaterialApi

@Composable
fun EventsScreen(
    extNavController: NavController,
    bottomPadding: Dp,
) {

    val viewModel: EventsViewModel = hiltViewModel()
    val events = viewModel.events.collectAsState(emptyList()).value
    val scaffoldState = rememberScaffoldState()

//    BottomNavItem.PersonItem.badgeCount.value = persons.size

    Scaffold(
        /*floatingActionButton = {
            currentGroup?.let {
                FloatingActionButton(
                    onClick = {
                        extNavController.navigate(Screen.NavNewPersonScreen.route)
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add person")
                }
            }
        },*/
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .padding(bottom = bottomPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            items(events) { event ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable {
//                            extNavController
                        },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            val modText = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
//                            val year = if (event.fullYear > 0) "[${event.fullYear}]" else ""
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    event.date,
                                    style = MaterialTheme.typography.h5,
                                    color = LightRed,
                                )
                                /*Text(
                                    year,
                                    style = MaterialTheme.typography.h6,
                                    color = YearColor,
                                )*/
                            }

                            Text(
                                event.getDescription(LocalContext.current),
                                modifier = modText,
                                style = MaterialTheme.typography.h6,
                                color = PrimaryDarkColor,
                            )

                            val person = viewModel.getPersonById(event.personId).collectAsState(
                                initial = null
                            ).value
                            person?.let {
                                Text(
                                    text = person.displayName,
                                    modifier = modText,
                                    style = MaterialTheme.typography.h5,
                                    color = MaterialTheme.colors.onBackground,
                                )
                                /*val groupName = viewModel.getGroupsById(person.groupId)?.name
                                Text(
                                    "[$groupName]",
                                    modifier = modText,
                                    style = MaterialTheme.typography.h5,
                                    color = OrangeYellow3,
                                )*/
                            }
                        }
                    }
                }
            }
        }
    }
}
