package ru.nifontbus.events_presenter

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.core.util.getLocalizedDate
import ru.nifontbus.core_ui.Screen
import ru.nifontbus.core_ui.component.SmallRememberImage
import ru.nifontbus.core_ui.cornerShapeIconPercent
import ru.nifontbus.core_ui.mediumPadding
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.persons_domain.model.Person

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
    getImage: suspend (id: Long) -> ImageBitmap?,
    isShowName: Boolean = true
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

                if (isShowName) {
                    person?.let {
                        Text(
                            text = it.displayName,
                            modifier = modText,
                            style = MaterialTheme.typography.h6,
                        )
                        Divider(modifier = Modifier.padding(horizontal = mediumPadding))
                    }
                }


                val dateText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Normal)) {
                        append(event.date.getLocalizedDate())
                    }
                    event.getFullYear()?.let {
                        val yearLeft = stringResource(R.string.sYearLeft, it)
                        if (it > 0) {
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.W300
                                )
                            ) {
                                append(yearLeft)
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
                        if (daysLeft > 0) stringResource(R.string.sDaysLeft, daysLeft) else "!!!"
                Text(
                    description,
                    modifier = modText.padding(bottom = 5.dp),
                    color = if (daysLeft == 0L) MaterialTheme.colors.error
                    else MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.h6
                )
            } // Column

            person?.let {
                SmallRememberImage(
                    personId = it.id,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(cornerShapeIconPercent)),
                    getImage = getImage
                )
            }
        } // Row
    }
}
