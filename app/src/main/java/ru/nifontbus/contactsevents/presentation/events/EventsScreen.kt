package ru.nifontbus.contactsevents.presentation.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
    Box(
//        shape = RoundedCornerShape(3.dp),
//        color = if (event.daysLeft() == 0L) MaterialTheme.colors.secondary
//        else MaterialTheme.colors.surface,
//        contentColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .background(
                color = if (event.daysLeft() == 0L) MaterialTheme.colors.secondary
                else MaterialTheme.colors.surface
            ),
    ) {
        val daysLeft = event.daysLeft()
        val person = viewModel.getPersonById(event.personId).collectAsState(null).value
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {

            val modText = Modifier.padding(horizontal = 10.dp)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                person?.let {
                    Text(
                        text = it.displayName,
                        modifier = modText,
                        style = MaterialTheme.typography.h6,
                    )
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
                        } else "!!!"
                Text(
                    description,
                    modifier = modText,
                    color = MaterialTheme.colors.primaryVariant,
                )
            } // Column
            person?.let {
                viewModel.getPhotoById(it.id)?.let { bmp ->
                    Image(
                        bitmap = bmp,
                        contentDescription = "Photo",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(50.dp)
                            .clip(RoundedCornerShape(100))

                    )
                }
            }
        } // Row
    }
}