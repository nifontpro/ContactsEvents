package ru.nifontbus.contactsevents.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.ui.theme.TextWhite

@Composable
fun TopBar(navController: NavController, header: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back",
                tint = MaterialTheme.colors.onBackground
            )
        }

        Text(
            text = header,
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun TopBarOther(
    header: String, imageOther: ImageVector,
    onBack: () -> Unit, onOther: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBack() },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back",
                tint = TextWhite
            )
        }

        Text(
            text = header,
            style = MaterialTheme.typography.h1
        )

        IconButton(
            onClick = { onOther() },
        ) {
            Icon(
                imageVector = imageOther, contentDescription = "Other",
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}
