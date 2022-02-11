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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.collect
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.domain.utils.getLocalizedDate
import ru.nifontbus.contactsevents.domain.utils.toLocalDate
import ru.nifontbus.contactsevents.domain.utils.toShortDate
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.presentation.navigation.TopBar
import ru.nifontbus.contactsevents.presentation.navigation.permission.GetPermission
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
    GetPermission(
        Manifest.permission.WRITE_CONTACTS,
        stringResource(R.string.sWriteDenied)
    ) {
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
            TopBar(navController = extNavController, header = stringResource(R.string.sNewEvent))
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

                val sYouCanCreateEvent = stringResource(R.string.sYouCanCreateEvent)
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PrimaryDarkColor)) {
                            append(sYouCanCreateEvent)
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
                            placeholder = { Text(stringResource(R.string.sEventLabel)) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    extNavController.navigate(Screen.ExtTemplatesScreen.route)
                                }) {
                                    Icon(imageVector = Icons.Default.ReadMore, null)
                                }
                            },
                        )

                        SelectDate(viewModel.date, viewModel.isNoYear)

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                viewModel.addEvent()
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            enabled = viewModel.isEnabledSave()
                        ) {
                            Text(
                                stringResource(R.string.sCreateEvent),
                                color = TextWhite,
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectDate(
    stateDate: MutableState<String>,
    isNoYear: MutableState<Boolean> = remember { mutableStateOf(false) }
) {

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

    Column {
        Button(
            onClick = {
                datePickerDialog.show()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(55.dp)

        ) {
            val sDate = stringResource(R.string.sDate)
            val txt = if (stateDate.value.isNotEmpty()) {
                if (!isNoYear.value) "$sDate ${stateDate.value.getLocalizedDate()}"
                else "$sDate ${stateDate.value.toShortDate().getLocalizedDate()}"
            } else stringResource(R.string.sSelectDate)
            Text(
                text = txt,
                color = TextWhite,
                style = MaterialTheme.typography.body1,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isNoYear.value,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                ),
                onCheckedChange = { isNoYear.value = it },
            )
            Text(stringResource(R.string.sWithoutYear))
        }

    }
}