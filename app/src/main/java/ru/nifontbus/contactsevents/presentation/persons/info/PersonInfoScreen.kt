package ru.nifontbus.contactsevents.presentation.persons.info

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
import androidx.compose.material.icons.filled.SettingsAccessibility
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.onebone.toolbar.*
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.utils.toPx
import ru.nifontbus.contactsevents.presentation.events.EventCard
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.navigation.TemplateSwipeToDismiss
import ru.nifontbus.contactsevents.presentation.navigation.TopBar
import ru.nifontbus.contactsevents.presentation.persons.SmallRememberImage
import ru.nifontbus.contactsevents.ui.theme.*

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
            /*TopBarOther("Person info", Icons.Default.Tune,
                { extNavController.popBackStack() },
                {
                    viewModel.getEditState()
                    navController.navigate(Screen.NavPersonEditScreen.route)
                })*/

            TopBar(extNavController, stringResource(R.string.sPersonInfo))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    extNavController.navigate(Screen.ExtNewEventScreen.createRoute(person.id))
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add event",
                    tint = TextWhite
                )
            }
        },
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = mediumPadding)
        ) {

            Icon(
                imageVector = Icons.Default.SettingsAccessibility,
                contentDescription = "Background",
                tint = Half3Gray,
                modifier = Modifier
                    .size(500.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
            )

            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp

            val collapsingState = rememberCollapsingToolbarScaffoldState(
                toolbarState = CollapsingToolbarState(initial = screenHeight.toPx().toInt() / 3)
            )

//            https://stackoverflow.com/questions/57727876/android-contacts-high-res-displayphoto-not-showing-up

            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = collapsingState,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbarModifier = Modifier
                    .background(Color.Transparent)
                    .padding(bottom = smallPadding),
                toolbar = {
//                    val offsetY = state.offsetY // y offset of the layout
                    val progress = collapsingState.toolbarState.progress

                    viewModel.displayPhoto?.let { it ->
                        Image(
                            bitmap = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .parallax(0.3f)
//                                .height(screenHeight / 3)
                                .graphicsLayer { alpha = progress * 1.3f },
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
                            .background(MaterialTheme.colors.surface)
                            .padding(mediumPadding)
                            .road(
                                whenCollapsed = Alignment.BottomStart,
                                whenExpanded = Alignment.BottomStart
                            ),
                        fontSize = (minFontSize + (maxFontSize - minFontSize) * progress).sp,
                        maxLines = if (progress < 0.2) 1 else 3
                    )

                    SmallRememberImage(
                        personId = person.id,
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
                        getImage = viewModel::getPhotoById
                    )
                } // toolbar
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = mediumPadding)
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
                                    event, person,
                                    onClick = {
                                        extNavController.navigate(
                                            Screen.ExtEventUpdateScreen.createRoute(
                                                person.id, event.id
                                            )
                                        )
                                    },
                                    getImage = { null }, // без картинки
                                    isShowName = false
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
            .background(Color.Transparent)
    ) {

        Box( // Внутренний полупрозрачный 1
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumPadding)
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
//                            horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Text(
                        groupsString,
                        modifier = Modifier.padding(bottom = mediumPadding),
                        style = MaterialTheme.typography.h6
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
                            tint = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier.padding(end = 15.dp)
                        )
                        Column(modifier = Modifier.fillMaxWidth()) {
                            personInfo.phones.forEachIndexed { idx, phone ->
                                Column {
                                    Text(phone.number, style = MaterialTheme.typography.h6)
                                    Text(phone.stringType(LocalContext.current))
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
        Text(
            stringResource(R.string.sEvents),
            modifier = Modifier.padding(vertical = smallPadding),
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.h5
        )
    } // Column Header
}