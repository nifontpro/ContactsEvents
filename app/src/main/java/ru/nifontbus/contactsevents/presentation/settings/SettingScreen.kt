package ru.nifontbus.contactsevents.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.nifontbus.contactsevents.R

@Composable
fun SettingScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.background,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "Contact",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = screenWidth / 5, top = 20.dp)
            )
            Text(
                text = "Events",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = screenWidth / 5, top = 10.dp, bottom = 20.dp)
            )

            Divider(modifier = Modifier.padding(horizontal = 20.dp))
            Text(
                text = "С любовью и заботой о ближних",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.VolunteerActivism,
            contentDescription = "Volunteer Activism",
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier
                .size(screenHeight / 2.8f)
                .align(Alignment.Center)
        )
        val iconSize = 50.dp
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(bottom = iconSize + 40.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isChecked = remember { mutableStateOf(false) }
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it })
                Text("Функции усопших")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isChecked = remember { mutableStateOf(false) }
                Checkbox(

                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it })
                Text("Считать 40-й день")
            }
        }
        BottomIcon(iconSize)
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