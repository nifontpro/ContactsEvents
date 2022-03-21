package ru.nifontbus.settings_presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.nifontbus.core_ui.bigPadding
import ru.nifontbus.core_ui.component.surfaceBrush
import ru.nifontbus.core_ui.normalPadding

@Composable
fun SettingScreen(paddingValues: PaddingValues) {

    val viewModel: SettingsViewModel = hiltViewModel()

    var showSettings by remember {
        mutableStateOf(false)
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showSettings = !showSettings
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colors.onSecondary,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.decor),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                alpha = 0.3f
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = stringResource(R.string.sContact),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = screenWidth / 6, top = bigPadding),
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = stringResource(R.string.sEventsSetting),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = screenWidth / 6, top = normalPadding, bottom = bigPadding),
                    color = MaterialTheme.colors.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.VolunteerActivism,
                contentDescription = "Volunteer Activism",
                tint = MaterialTheme.colors.error.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(screenHeight / 2.8f)
                    .align(Alignment.Center)
            )
            val iconSize = 50.dp

            AnimatedVisibility(
                visible = showSettings,
                modifier = Modifier.align(Alignment.BottomStart),
            ) {
                SettingBox(iconSize, viewModel)
            }
            BottomIcon(iconSize)
        }
    }
}

@Composable
private fun SettingBox(iconSize: Dp, viewModel: SettingsViewModel) {

    val settings = viewModel.settings.collectAsState().value

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = iconSize + 40.dp)
            .padding(horizontal = 40.dp),
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceBrush())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = bigPadding),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
//                        checked = viewModel.notificationState.collectAsState().value,
                        checked = settings.showNotifications,
                        colors = checkboxColors(),
                        onCheckedChange = { viewModel.setNotificationState(it) },
                    )
                    Text(
                        stringResource(R.string.sShowNotifications)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
//                        checked = viewModel.reposeFeatures.collectAsState().value,
                        checked = settings.reposeFeatures,
                        colors = checkboxColors(),
                        onCheckedChange = { viewModel.setReposeFeatures(it) },
                    )
                    Text(
                        stringResource(R.string.sReposeFeatures)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
//                        checked = viewModel.add40Day.collectAsState().value,
                        checked = settings.add40Day,
                        colors = checkboxColors(),
                        enabled = settings.reposeFeatures,
                        onCheckedChange = { viewModel.setAdd40Day(it) }
                    )
                    Text(
                        stringResource(R.string.s40Day),
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.syncAll() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = normalPadding),
                    border = BorderStroke(2.dp, Color.Red),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(R.string.sSyncing),
                        style = MaterialTheme.typography.h5
                    )
                }
            } // Column
        }
    }
}

@Composable
private fun checkboxColors() = CheckboxDefaults.colors(
    checkedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
)

@Composable
private fun BoxScope.BottomIcon(iconSize: Dp) {

    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(bottom = bigPadding)
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_kotlin_icon),
            contentDescription = "",
            modifier = Modifier
                .size(iconSize)
        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_compose),
            contentDescription = "",
            modifier = Modifier
                .size(iconSize)
        )
    }
}
