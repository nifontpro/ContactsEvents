package ru.nifontbus.templates_presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import ru.nifontbus.core_ui.Argument
import ru.nifontbus.core_ui.component.TemplateSwipeToDismiss
import ru.nifontbus.core_ui.component.TopBar
import ru.nifontbus.core_ui.component.surfaceBrush
import ru.nifontbus.core_ui.normalPadding
import ru.nifontbus.core_ui.smallPadding
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.templates_domain.model.Template

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
                    onDelete = {
//                        viewModel.deleteTemplate(template)
                    },
                    enabled = false // template.type == 0,
                ) {
                    TemplateCard(template) {
                        extNavController.previousBackStackEntry?.arguments?.putParcelable(
                            Argument.template,
                            Template(
                                type = template.type,
                                label = template.getDescriptionForSelect(context)
                            )
                        )
                        extNavController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: Template,
    onSelect: () -> Unit = {},
) {
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(vertical = smallPadding)
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceBrush()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                template.getDescription(LocalContext.current),
                modifier = Modifier.padding(normalPadding),
                style = MaterialTheme.typography.h5,
            )
            if (template.type == EventType.CUSTOM) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    modifier = Modifier.padding(normalPadding),
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
            if (template.type == EventType.BIRTHDAY || template.type == EventType.ANNIVERSARY ||
                template.type == EventType.OTHER
            ) {
                Icon(
                    imageVector = Icons.Outlined.Android,
                    contentDescription = "",
                    modifier = Modifier.padding(normalPadding),
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}