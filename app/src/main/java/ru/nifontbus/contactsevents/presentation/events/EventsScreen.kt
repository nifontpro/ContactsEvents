package ru.nifontbus.contactsevents.presentation.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.domain.data.Event

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
                EventCard(event, viewModel)
            }
        }
    }
}

@Composable
private fun EventCard(
    event: Event,
    viewModel: EventsViewModel
) {
    Surface(
        elevation = 1.dp,
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysLeft = event.daysLeft()
            val person = viewModel.getPersonById(event.personId).collectAsState(null).value
            val modText = Modifier.padding(horizontal = 10.dp)
            Column {

                person?.let {
                    Text(
                        text = it.displayName,
                        modifier = modText,
                        style = MaterialTheme.typography.h6,
                    )
                    /*val groupName = viewModel.getGroupsById(person.groupId)?.name
                                Text(
                                    "[$groupName]",
                                    modifier = modText,
                                    style = MaterialTheme.typography.h5,
                                    color = OrangeYellow3,
                                )*/
                    Divider()
                }

                val dateText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Normal)) {
                        append(event.date)
                    }
                    event.getFullYear()?.let {
                        if (it > 0) {
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.W300
                                )
                            ) {
                                append(", прошло лет: $it")
                            }
                        }
                    }
                }
                Text(
                    dateText,
                    modifier = modText,
                    style = MaterialTheme.typography.h6,
                )

                val description = event.getDescription(LocalContext.current) +
                        if (daysLeft > 0) {
                            ", осталось дней: $daysLeft"
                        } else ""
                Text(
                    description,
                    modifier = modText,
                    color = MaterialTheme.colors.primaryVariant,
                )
            } // Column
        } // Row
    }
}
