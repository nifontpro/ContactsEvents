package ru.nifontbus.contactsevents.presentation.events.update

import android.Manifest
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReadMore
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collect
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.domain.utils.toLocalDate
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.navigation.TopBar
import ru.nifontbus.contactsevents.ui.theme.IconGreen
import ru.nifontbus.contactsevents.ui.theme.LightGreen2
import ru.nifontbus.contactsevents.ui.theme.PrimaryDarkColor
import ru.nifontbus.contactsevents.ui.theme.TextWhite
import java.time.LocalDate

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun NewEventScreen(
    extNavController: NavHostController,
    returnTemplate: Template?,
) {
    GetWriteContactsPermission {
        NewEventScreenMain(extNavController, returnTemplate)
    }
}

@ExperimentalComposeUiApi
@Composable
fun NewEventScreenMain(
    extNavController: NavHostController,
    returnTemplate: Template?,
) {
    val viewModel: EventUpdateViewModel = hiltViewModel()
    val scaffoldState = rememberScaffoldState()
    val person = viewModel.person.value

    LaunchedEffect(returnTemplate) {
        returnTemplate?.let {
            viewModel.setEventLabel(returnTemplate.label)
            viewModel.eventType.value = returnTemplate.type
        }
    }

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(scaffoldState = scaffoldState, backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBar(navController = extNavController, header = "New event")
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Icon(
                imageVector = Icons.Outlined.NotificationAdd, contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .size(350.dp),
                tint = IconGreen
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PrimaryDarkColor)) {
                            append("You can create new event for person ")
                        }
                        withStyle(style = SpanStyle(color = LightGreen2)) {
                            append(person.displayName)
                        }
                    },
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.h6
                )
                Box( // Внутренний полупрозрачный
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

                        val edMod = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(5.dp))

                        val keyboardController = LocalSoftwareKeyboardController.current

                        TextField(
                            value = viewModel.eventLabel.value,
                            enabled = viewModel.isEnabledEdit(),
                            onValueChange = {
                                viewModel.setEventLabel(it)
                            },
                            modifier = edMod,
                            singleLine = true,
                            placeholder = { Text("Enter event") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    extNavController.navigate(Screen.ExtTemplatesScreen.route)
                                }) {
                                    Icon(imageVector = Icons.Default.ReadMore, null)
                                }
                            },
                        )

                        SelectDate(viewModel.date)

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                viewModel.addEvent()
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            enabled = viewModel.isEnabledSave()
                        ) {
                            Text("Create event", color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun GetWriteContactsPermission(content: @Composable (() -> Unit)) {
    val writeContact = rememberPermissionState(Manifest.permission.WRITE_CONTACTS)
    PermissionRequired(
        permissionState = writeContact,
        permissionNotGrantedContent = {
            LaunchedEffect(true) {
                writeContact.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    "Write contacts permission denied. See this FAQ with information about why we " +
                            "need this permission. Please, grant us access on the Settings screen."
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {}) {
                    Text("Open Settings")
                }
            }
        },
        content = content
    )
}

@Composable
fun SelectDate(stateDate: MutableState<String>) {

    val context = LocalContext.current
    val localDate = if (stateDate.value.isNotEmpty()) stateDate.value.toLocalDate()
    else LocalDate.now()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val date = LocalDate.of(year, month + 1, day)
            stateDate.value = date.toString()
        }, localDate.year, localDate.monthValue - 1, localDate.dayOfMonth
    )

    Button(
        onClick = {
            datePickerDialog.show()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(55.dp)

    ) {
        Text(
            text = if (stateDate.value.isNotEmpty()) "Date: ${stateDate.value}"
            else "Select date",
            color = TextWhite
        )
    }
}