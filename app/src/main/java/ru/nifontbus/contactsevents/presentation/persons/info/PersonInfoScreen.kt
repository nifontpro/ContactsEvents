package ru.nifontbus.contactsevents.presentation.persons.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collect
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.navigation.TemplateSwipeToDismiss
import ru.nifontbus.contactsevents.presentation.navigation.TopBar
import ru.nifontbus.contactsevents.ui.theme.Half3Gray
import ru.nifontbus.contactsevents.ui.theme.TextWhite

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun PersonInfoScreen(extNavController: NavHostController) {

//    val navController = rememberNavController()

/*    NavHost(
        navController = navController,
        startDestination = Screen.NavPersonInfoMainScreen.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(Screen.NavPersonInfoMainScreen.route) {
            PersonInfoMainScreen(extNavController, navController, viewModel)
        }

        composable(Screen.NavPersonEditScreen.route) {
            PersonEditScreen(navController, viewModel) {
                navController.popBackStack()
                extNavController.popBackStack()
            }
        }
    }*/

    PersonInfoMainScreen(extNavController)

}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun PersonInfoMainScreen(
    extNavController: NavHostController,
//    navController: NavHostController,
//    viewModel: PersonInfoViewModel
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

    Scaffold(scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            /*TopBarOther("Person info", Icons.Default.Tune,
                { extNavController.popBackStack() },
                {
                    viewModel.getEditState()
                    navController.navigate(Screen.NavPersonEditScreen.route)
                })*/

            TopBar(extNavController, "Person info")
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Icon(
                imageVector = Icons.Default.SettingsAccessibility,
                contentDescription = "Background",
                tint = Half3Gray,
                modifier = Modifier
                    .size(500.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 10.dp)
//                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {

                TextPerson(person.displayName, MaterialTheme.colors.primaryVariant)

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
                            .padding(20.dp)
                    ) {

                        var groupsString = ""
                        val delimetr = stringResource(R.string.sDelimetr)
                        person.groups.forEach { groupId ->
                            viewModel.getGroupById(groupId)?.let {
                                groupsString += it.localTitle(LocalContext.current) + delimetr
                            }
                        }
                        groupsString = stringResource(R.string.sGroups) +
                                groupsString.removeSuffix(delimetr)

                        Text(
                            groupsString,
                            modifier = Modifier.padding(bottom = 10.dp),
                            style = MaterialTheme.typography.h6
                        )

                        val personInfo = viewModel.getPersonInfo(person.id)
                        if (personInfo.phones.isNotEmpty()) {
                            Divider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "phone",
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

                        Button(
                            onClick = {
                                extNavController.navigate(
                                    Screen.NavNewEventScreen.createRoute(person.id)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .height(55.dp),
                        ) {
                            Text("Create new event", color = TextWhite)
                        }
                    }
                } // Box1
                Text(
                    "Events: ",
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.h5
                )
                Box( // Внутренний полупрозрачный 2
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 10.dp)
                    ) {

                        itemsIndexed(
                            items = personEvents,
                            key = { _, item -> item.id }
                        ) { _, event ->

                            TemplateSwipeToDismiss(
                                modifier = Modifier.padding(vertical = 4.dp),
                                {
                                    viewModel.deleteEvent(event)
                                },
                                {
                                    EventInfoCard(event)
                                },
//                                enabled = template.type == 0,
                            )
                        }
                    } // LazyColumn
                } // Box2
            } // Column
        }
    }
}

@Composable
private fun EventInfoCard(event: Event) {
    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${event.date} id: ${event.id} type: ${event.type}",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                style = MaterialTheme.typography.h6,
            )
            Text(
                event.getDescription(LocalContext.current),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primaryVariant,
            )
        }
    }
}

@Composable
fun TextPerson(text: String, color: Color = MaterialTheme.colors.onBackground) {
    Text(
        text,
        modifier = Modifier.padding(bottom = 5.dp),
        color = color,
        style = MaterialTheme.typography.h5
    )
}