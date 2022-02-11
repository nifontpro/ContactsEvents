package ru.nifontbus.contactsevents.presentation.events.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.presentation.navigation.TemplateSwipeToDismiss
import ru.nifontbus.core_ui.Arg
import ru.nifontbus.core_ui.TopBar
import ru.nifontbus.events_domain.model.EventType

@ExperimentalMaterialApi
@Composable
fun TemplatesScreen(
    extNavController: NavController,
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
            TopBar(extNavController, stringResource(R.string.sTypeEvents))
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
        val context = LocalContext.current
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
                            extNavController.previousBackStackEntry?.arguments?.putParcelable(
                                Arg.template,
                                Template(
                                    type = template.type,
                                    label = template.getDescriptionForSelect(context)
                                )
                            )
                            extNavController.popBackStack()
                        }
                    },
                    enabled = false // template.type == 0,
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun TemplateCard(
    template: Template,
    onSelect: () -> Unit,
) {
    Surface(
        onClick = onSelect,
        elevation = 1.dp,
        shape = RoundedCornerShape(3.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                template.getDescription(LocalContext.current),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                style = MaterialTheme.typography.h5,
            )
            if (template.type == EventType.CUSTOM) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    modifier = Modifier.padding(10.dp),
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
            if (template.type == EventType.BIRTHDAY || template.type == EventType.ANNIVERSARY ||
                template.type == EventType.OTHER
            ) {
                Icon(
                    imageVector = Icons.Outlined.Android,
                    contentDescription = "",
                    modifier = Modifier.padding(10.dp),
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}