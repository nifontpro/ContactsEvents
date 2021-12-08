package ru.nifontbus.contactsevents.presentation.events.new_event.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ru.nifontbus.contactsevents.domain.data.EventType
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.presentation.navigation.TemplateSwipeToDismiss
import ru.nifontbus.contactsevents.presentation.navigation.TopBar
import ru.nifontbus.contactsevents.ui.theme.LightRedHalf

@ExperimentalMaterialApi
@Composable
fun TemplatesScreen(
    extNavController: NavController,
    sharedTemplateState: MutableState<Template>
) {
    val viewModel: TemplateViewModel = hiltViewModel()
    val scaffoldState = rememberScaffoldState()
    val templates = viewModel.templates.value

    LaunchedEffect(scaffoldState) {
        viewModel.action.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopBar(extNavController, "Templates of events")
        },
        /*floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.NavNewTemplateScreen.route) },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add template")
            }
        },*/
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            itemsIndexed(
                items = templates,
                key = { _, item -> item.id }
            ) { _, template ->

                TemplateSwipeToDismiss(
                    modifier = Modifier.padding(vertical = 4.dp),
                    {
//                        viewModel.deleteTemplate(template)
                    },
                    {
                        TemplateCard(template) {
                            sharedTemplateState.value = template
                            extNavController.popBackStack()
                        }
                    },
                    enabled = false // template.type == 0,
                )
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: Template,
    onSelect: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { onSelect() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    template.name,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    style = MaterialTheme.typography.h5,
                )
                if (template.type != EventType.CUSTOM) {
                    Icon(
                        imageVector = Icons.Outlined.LockClock, contentDescription = "Lock",
                        modifier = Modifier.padding(10.dp),
                        tint = LightRedHalf
                    )
                }
            }
        }
    }
}
