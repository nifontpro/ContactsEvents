package ru.nifontbus.settings_presenter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colors.background)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = stringResource(R.string.sContact),
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = screenWidth / 6, top = bigPadding),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(R.string.sEventsSetting),
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = screenWidth / 6, top = normalPadding, bottom = bigPadding),
                color = MaterialTheme.colors.onSurface
            )

            Divider(
                modifier = Modifier.padding(horizontal = bigPadding),
                color = MaterialTheme.colors.onSurface
            )
        }
        Icon(
            imageVector = Icons.Default.VolunteerActivism,
            contentDescription = "Volunteer Activism",
            tint = MaterialTheme.colors.error.copy(alpha = 0.4f),
            modifier = Modifier
                .size(screenHeight / 2.8f)
                .align(Alignment.Center)
        )
        val iconSize = 50.dp
        SettingBox(iconSize, viewModel)
        BottomIcon(iconSize)
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
            .padding(horizontal = bigPadding),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceBrush())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.reposeFeatures.collectAsState().value,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        ),
                        onCheckedChange = { viewModel.setReposeFeatures(it) },
                    )
                    Text(
                        stringResource(R.string.sReposeFeatures)
                    )
                }

//            if (viewModel.reposeFeatures.collectAsState().value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.add40Day.collectAsState().value,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        ),
                        enabled = viewModel.reposeFeatures.collectAsState().value,
                        onCheckedChange = { viewModel.setAdd40Day(it) }
                    )
                    Text(
                        stringResource(R.string.s40Day),
//                    color = MaterialTheme.colors.onPrimary
                    )
                }
            } // Column
        }
    }
}

@Composable
private fun BoxScope.BottomIcon(iconSize: Dp) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_android_studio),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(iconSize)

        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_kotlin_icon),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(iconSize)

        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_compose),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(iconSize)
        )
    }
}