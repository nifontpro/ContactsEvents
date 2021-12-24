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
import androidx.compose.ui.graphics.ImageBitmap
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
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.utils.getLocalizedDate
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.persons.SmallRememberImage
import ru.nifontbus.contactsevents.ui.theme.cornerShapeIconPercent
import ru.nifontbus.contactsevents.ui.theme.mediumPadding

@ExperimentalMaterialApi

@Composable
fun EventsScreen(
    extNavController: NavController,
    bottomPadding: Dp,
) {

    val viewModel: EventsViewModel = hiltViewModel()
    val events = viewModel.events.collectAsState(emptyList())

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
            items(events.value) { event ->
                val person = viewModel.getPersonByIdFlow(event.personId)
                    .collectAsState(null).value
                EventCard(
                    event, person,
                    onClick = {
                        person?.let {
                            extNavController.navigate(
                                Screen.ExtPersonInfoScreen.createRoute(it.id)
                            )
                        }
                    },
                    getImage = viewModel::getPhotoById // { viewModel.getPhotoById(it) } - Eq
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    person: Person?,
    onClick: () -> Unit = {},
    getImage: suspend (id: Long) -> ImageBitmap?
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 3.dp)
            .background(
                color = if (event.daysLeft() == 0L) MaterialTheme.colors.secondary
                else MaterialTheme.colors.surface
            ),
    ) {
        val daysLeft = event.daysLeft()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {

            val modText = Modifier.padding(horizontal = mediumPadding)
            Column(
                modifier = Modifier.weight(5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
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
                        append(event.date.getLocalizedDate())
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
                    color = if (daysLeft == 0L) MaterialTheme.colors.error
                    else MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1
                )
            } // Column

            person?.let {

                SmallRememberImage(
                    personId = it.id,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                        .size(50.dp)
                        .clip(RoundedCornerShape(cornerShapeIconPercent)),
                    getImage = getImage
                )
            }
        } // Row
    }
}
