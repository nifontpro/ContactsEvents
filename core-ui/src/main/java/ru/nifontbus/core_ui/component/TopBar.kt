package ru.nifontbus.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.nifontbus.core_ui.normalPadding
import ru.nifontbus.core_ui.smallPadding

@Composable
fun TopBar(navController: NavController, header: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 12.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(start = normalPadding, top = smallPadding, bottom = smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
                    tint = MaterialTheme.colors.onSecondary
                )
            }

            Text(
                text = header,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onSecondary
            )
        }
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
                tint = MaterialTheme.colors.onSecondary
            )
        }

        Text(
            text = header,
            style = MaterialTheme.typography.h5
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
