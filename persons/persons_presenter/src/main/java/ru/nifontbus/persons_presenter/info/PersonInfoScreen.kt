package ru.nifontbus.persons_presenter.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import ru.nifontbus.core_ui.*
import ru.nifontbus.core_ui.component.TemplateSwipeToDismiss
import ru.nifontbus.core_ui.component.TopBar
import ru.nifontbus.core_ui.component.surfaceBrush
import ru.nifontbus.events_presenter.EventCard
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_presenter.R

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun PersonInfoScreen(
    extNavController: NavHostController,
) {
    val viewModel: PersonInfoViewModel = hiltViewModel()
    val person = viewModel.person.value
    val personEvents = viewModel.personEvents.collectAsState(emptyList()).value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBar(extNavController, stringResource(R.string.sPersonInfo))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    extNavController.navigate(Screen.ExtNewEventScreen.createRoute(person.id))
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add event",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        },
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = normalPadding)
        ) {

            val collapsingState = rememberCollapsingToolbarScaffoldState()
            /*    toolbarState = CollapsingToolbarState(
                    initial = (maxHeight.toPx() / 2.5f).toInt()
                )
            )*/

//            https://stackoverflow.com/questions/57727876/android-contacts-high-res-displayphoto-not-showing-up

            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = collapsingState,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbarModifier = Modifier
                    .background(Transparent)
                    .padding(bottom = smallPadding),
                toolbar = {
//                    val offsetY = state.offsetY // y offset of the layout
                    val progress = collapsingState.toolbarState.progress

                    viewModel.displayPhoto.value?.let { it ->
                        Image(
                            bitmap = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .parallax(0.3f)
                                .graphicsLayer { alpha = progress * 1.3f }
                                .road(
                                    whenCollapsed = Alignment.Center,
                                    whenExpanded = Alignment.Center
                                ),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null
                        )
                    }

                    val minFontSize = personInfoToolBarMinFontSize
                    val maxFontSize = personInfoToolBarMaxFontSize
                    Text(
                        person.displayName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceBrush())
                            .padding(normalPadding)
                            .road(
                                whenCollapsed = Alignment.BottomStart,
                                whenExpanded = Alignment.BottomStart
                            ),
                        fontSize = (minFontSize + (maxFontSize - minFontSize) * progress).sp,
                        maxLines = if (progress < 0.2) 1 else 3
                    )

                    person.photo?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Photo",
                            modifier = Modifier
                                .padding(smallPadding)
                                .size(personInfoSmallIconSize)
                                .clip(RoundedCornerShape(10))
                                .graphicsLayer {
                                    alpha = maxOf(0f, 1 - progress * 3)
                                }
                                .road(
                                    whenCollapsed = Alignment.BottomEnd,
                                    whenExpanded = Alignment.BottomEnd
                                ),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                } // toolbar
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = normalPadding)
                ) {

                    item {
                        PersonInfoHeader(person, viewModel)
                    }

                    itemsIndexed(
                        items = personEvents,
                        key = { _, item -> item.id }
                    ) { _, event ->

                        TemplateSwipeToDismiss(
                            modifier = Modifier.padding(bottom = smallPadding),
                            {
                                viewModel.deleteEvent(event)
                            },
                            {
                                EventCard(
                                    modifier = Modifier.padding(horizontal = normalPadding),
                                    event = event,
                                    person = person,
                                    onClick = {
                                        extNavController.navigate(
                                            Screen.ExtEventUpdateScreen.createRoute(
                                                person.id, event.id
                                            )
                                        )
                                    },
                                    isShowNameAndImage = false,
                                )
                            },
//                                enabled = template.type == 0,
                        )
                    }
                } // LazyColumn
            }
        }
    }
}

@Composable
private fun PersonInfoHeader(
    person: Person,
    viewModel: PersonInfoViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = normalPadding)
            .padding(top = normalPadding)
            .background(Color.Transparent)
    ) {

        Card(
            shape = MaterialTheme.shapes.large,
            elevation = 4.dp
        ) {
            Box( // Внутренний полупрозрачный 1
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceBrush())
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(normalPadding)
                ) {
                    var groupsString = ""
                    val delimetr = stringResource(R.string.sDelimetr)
                    person.groups.forEach { groupId ->
                        //LE
                        viewModel.getGroupById(groupId)?.let {
                            groupsString += it.localTitle(LocalContext.current) + delimetr
                        }
                    }
                    groupsString = stringResource(R.string.sGroups) +
                            groupsString.removeSuffix(delimetr)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        Text(
                            text = groupsString,
                            modifier = Modifier.padding(bottom = normalPadding),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface
                        )
                    } // Row

                    val personInfo = viewModel.personInfo.value
                    if (personInfo.phones.isNotEmpty()) {
                        Divider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.padding(end = 15.dp)
                            )
                            Column(modifier = Modifier.fillMaxWidth()) {
                                personInfo.phones.forEachIndexed { idx, phone ->
                                    Column {
                                        Text(
                                            text = phone.number,
                                            style = MaterialTheme.typography.h6,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                        Text(
                                            text = phone.stringType(LocalContext.current),
                                            color = MaterialTheme.colors.onSurface
                                        )
                                        if (idx < personInfo.phones.lastIndex) {
                                            Divider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } // Box
        } // Card
        Text(
            stringResource(R.string.sEvents),
            modifier = Modifier.padding(vertical = smallPadding),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5
        )
    } // Column Header
}