package ru.nifontbus.events_presenter.update

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReadMore
import androidx.compose.material.icons.outlined.EditNotifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ru.nifontbus.core_ui.Screen
import ru.nifontbus.core_ui.bigPadding
import ru.nifontbus.core_ui.component.TopBar
import ru.nifontbus.core_ui.component.surfaceBrush
import ru.nifontbus.core_ui.normalPadding
import ru.nifontbus.core_ui.permission.GetPermission
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_presenter.R
import ru.nifontbus.templates_domain.model.Template

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun EventUpdateScreen(
    extNavController: NavHostController,
    returnTemplate: Template?
) {
    GetPermission(
        Manifest.permission.WRITE_CONTACTS,
        stringResource(R.string.sWriteDenied)
    ) {
        EventUpdateScreenMain(extNavController, returnTemplate)
    }
}

@ExperimentalComposeUiApi
@Composable
fun EventUpdateScreenMain(
    extNavController: NavHostController,
    returnTemplate: Template?
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

    val context = LocalContext.current
    LaunchedEffect(viewModel.eventType.value) {
        viewModel.setEventLabel(
            Event.getTypeLabel(
                type = viewModel.eventType.value,
                label = viewModel.eventLabel.value,
                context = context
            )
        )
    }

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(scaffoldState = scaffoldState, backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBar(navController = extNavController, header = stringResource(R.string.sUpdateEvent))
        }
    ) {

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            Icon(
                imageVector = Icons.Outlined.EditNotifications, contentDescription = null,
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

                val youCanUpdateEvent = stringResource(R.string.sYouCanUpdateEvent)
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                            append(youCanUpdateEvent)
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

                }
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
                                viewModel.updateEvent()
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            enabled = viewModel.isEnabledUpdate(),
                            colors = buttonColors()
                        ) {
                            Text(
                                text= stringResource(R.string.sUpdateEvent),
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    }
                }
            }
        }
    }
}