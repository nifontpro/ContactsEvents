package ru.nifontbus.events_presenter.update

import android.Manifest
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import ru.nifontbus.core.util.getLocalizedDate
import ru.nifontbus.core.util.toLocalDate
import ru.nifontbus.core.util.toShortDate
import ru.nifontbus.core_ui.Screen
import ru.nifontbus.core_ui.bigPadding
import ru.nifontbus.core_ui.component.TopBar
import ru.nifontbus.core_ui.component.surfaceBrush
import ru.nifontbus.core_ui.normalPadding
import ru.nifontbus.core_ui.permission.GetPermission
import ru.nifontbus.events_presenter.R
import ru.nifontbus.templates_domain.model.Template
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

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            Icon(
                imageVector = Icons.Outlined.NotificationAdd, contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bigPadding)
                    .size(maxHeight / 2),
                tint = MaterialTheme.colors.secondary.copy(alpha = 0.2f)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = normalPadding)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {

                val sYouCanCreateEvent = stringResource(R.string.sYouCanCreateEvent)
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                            append(sYouCanCreateEvent)
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.primaryVariant)) {
                            append(person.displayName)
                        }
                    },
                    modifier = Modifier.padding(normalPadding),
                    style = MaterialTheme.typography.h5
                )


                Card(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 8.dp
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceBrush())
                    ) {

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bigPadding)
                        ) {

                            val edMod = Modifier
                                .fillMaxWidth()
                                .padding(bottom = normalPadding)
                                .clip(MaterialTheme.shapes.small)

                            val keyboardController = LocalSoftwareKeyboardController.current

                            OutlinedTextField(
                                value = viewModel.eventLabel.value,
                                enabled = viewModel.isEnabledEdit(),
                                onValueChange = {
                                    viewModel.setEventLabel(it)
                                },
                                modifier = edMod,
                                singleLine = true,
                                placeholder = { Text(stringResource(R.string.sEventLabel)) },
                                colors = textFieldColors(),
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
                                enabled = viewModel.isEnabledSave(),
                                colors = buttonColors()
                            ) {
                                Text(
                                    text = stringResource(R.string.sCreateEvent),
                                    style = MaterialTheme.typography.body1,
                                )
                            }
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
                .height(55.dp),
            colors = buttonColors()

        ) {
            val sDate = stringResource(R.string.sDate)
            val txt = if (stateDate.value.isNotEmpty()) {
                if (!isNoYear.value) "$sDate ${stateDate.value.getLocalizedDate()}"
                else "$sDate ${stateDate.value.toShortDate().getLocalizedDate()}"
            } else stringResource(R.string.sSelectDate)
            Text(
                text = txt,
                color = MaterialTheme.colors.onSecondary,
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

@Composable
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = MaterialTheme.colors.onBackground,
    backgroundColor = MaterialTheme.colors.surface,
    cursorColor = MaterialTheme.colors.secondary,
    focusedIndicatorColor = MaterialTheme.colors.secondary,
    unfocusedIndicatorColor = MaterialTheme.colors.onSurface,
    placeholderColor = MaterialTheme.colors.onSurface,
    leadingIconColor = MaterialTheme.colors.onSurface,
    trailingIconColor = MaterialTheme.colors.onSurface,
)

@Composable
fun buttonColors() =
    ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary,
        disabledBackgroundColor = MaterialTheme.colors.background.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled)
    )