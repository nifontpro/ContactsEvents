package ru.nifontbus.settings_presenter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.syncAll()
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sync",
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
            SettingBox(iconSize, viewModel)
            BottomIcon(iconSize)
        }
    }
}

@Composable
private fun BoxScope.SettingBox(iconSize: Dp, viewModel: SettingsViewModel) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
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
                        checked = viewModel.notificationState.collectAsState().value,
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
                        checked = viewModel.reposeFeatures.collectAsState().value,
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
                        checked = viewModel.add40Day.collectAsState().value,
                        colors = checkboxColors(),
                        enabled = viewModel.reposeFeatures.collectAsState().value,
                        onCheckedChange = { viewModel.setAdd40Day(it) }
                    )
                    Text(
                        stringResource(R.string.s40Day),
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

/*    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

    }*/
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
