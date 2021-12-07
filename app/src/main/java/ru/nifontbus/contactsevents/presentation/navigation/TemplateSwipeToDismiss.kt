package ru.nifontbus.contactsevents.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.nifontbus.contactsevents.ui.theme.DeleteColor
import ru.nifontbus.contactsevents.ui.theme.TextWhite
import ru.nifontbus.contactsevents.ui.theme.Transparent

@ExperimentalMaterialApi
@Composable
fun TemplateSwipeToDismiss(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
    enabled: Boolean = true,
) {

    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        LaunchedEffect(dismissState) {
            onDelete()
            dismissState.reset()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        directions = if (enabled) setOf(DismissDirection.EndToStart) else emptySet(),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.7f)
        },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Transparent
                    DismissValue.DismissedToEnd -> Color.Transparent
                    DismissValue.DismissedToStart -> DeleteColor
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Delete
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            val tintColor = when (direction) {
                DismissDirection.StartToEnd -> Transparent
                DismissDirection.EndToStart -> TextWhite
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.scale(scale),
                    tint = tintColor
                )
            }
        },
        dismissContent = dismissContent
    )
}