package ru.nifontbus.events_presenter

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
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
import ru.nifontbus.core_ui.*
import ru.nifontbus.core_ui.component.SmallRememberImage
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

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .padding(bottom = bottomPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = normalPadding, vertical = 3.dp)
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
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        border = if (event.daysLeft() == 0L) BorderStroke(1.dp, MaterialTheme.colors.onBackground)
        else null,
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colors.surface,
                            MaterialTheme.colors.onError
                        )
                    )
                )
        )
        {

            val daysLeft = event.daysLeft()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = normalPadding),
            ) {

                val modText = Modifier.padding(vertical = smallPadding)
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
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onBackground
                            )
                            Divider(
                                modifier = Modifier.padding(end = normalPadding),
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }

                    val description = event.getDescription(LocalContext.current)
                    Text(
                        text = "${event.date.getLocalizedDate()}: $description",
                        modifier = modText,
                        color = if (daysLeft == 0L) MaterialTheme.colors.primaryVariant
                        else MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h6
                    )

                    val dateText = buildAnnotatedString {

                        event.getFullYear()?.let {
                            val yearLeft = stringResource(R.string.sYearLeft, it)
                            if (it > 0) {
                                /*withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium
                                    )
                                ) {*/
                                    append(yearLeft)
//                                }
                            }
                        }

                        val daysLeftString =
                            if (daysLeft > 0) stringResource(
                                R.string.sDaysLeft,
                                daysLeft
                            ) else stringResource(R.string.sNow)

//                        withStyle(style = SpanStyle(fontStyle = FontStyle.Normal)) {
                            append(daysLeftString)
//                        }
                    }
                    Text(
                        text = dateText,
                        modifier = Modifier.padding(bottom = smallPadding),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                } // Column

                person?.let {
                    SmallRememberImage(
                        personId = it.id,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(cornerShapeIconPercent)),
                        getImage = getImage
                    )
                }
            } // Row
        }
    }
}
