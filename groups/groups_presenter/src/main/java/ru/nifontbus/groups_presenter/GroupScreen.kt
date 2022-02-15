package ru.nifontbus.groups_presenter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.nifontbus.core_ui.component.BottomNavItem
import ru.nifontbus.groups_domain.model.PersonsGroup

@ExperimentalMaterialApi
@Composable
fun GroupScreen(
    bottomPadding: Dp,
) {
    val viewModel: GroupViewModel = hiltViewModel()
    val currentGroup = viewModel.currentGroup.collectAsState().value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    val groups = viewModel.groups.collectAsState(emptyList()).value
    BottomNavItem.GroupItem.badgeCount.value = groups.size

    Scaffold(
        /*floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    extNavController.navigate(Screen.NavNewGroupScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add group")
            }
        },*/
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(bottom = bottomPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            items(groups) { group ->
                GroupCard(group, viewModel, currentGroup)
            }
        }
    }
}

@Composable
private fun GroupCard(
    group: PersonsGroup,
    viewModel: GroupViewModel,
    currentGroup: PersonsGroup?
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "[${group.localTitle(LocalContext.current)}]",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    group.account,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
            }
            IconButton(
                onClick = { viewModel.setCurrentGroup(group) },
            ) {
                Icon(
                    imageVector = if (group.id == currentGroup?.id) {
                        Icons.Default.DoneOutline
                    } else {
                        Icons.Default.RadioButtonUnchecked
                    },
                    contentDescription = "Current group",
                    tint = if (group.id == currentGroup?.id) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        Color.Gray
                    }
                )
            }
        }
    }
}

