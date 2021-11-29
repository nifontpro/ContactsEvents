package ru.nifontbus.contactsevents.presentation.persons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.presentation.navigation.BottomNavItem
import ru.nifontbus.contactsevents.ui.theme.*

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun PersonsScreen(
    extNavController: NavController,
    bottomPadding: Dp,
) {
    val viewModel: PersonsViewModel = hiltViewModel()
    val scaffoldState = rememberScaffoldState()
    val persons = viewModel.persons.collectAsState(emptyList()).value

    BottomNavItem.PersonItem.badgeCount.value = persons.size

    Scaffold(
        /*floatingActionButton = {
            currentGroup?.let {
                FloatingActionButton(
                    onClick = {
                        extNavController.navigate(Screen.NavNewPersonScreen.route)
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add person")
                }
            }
        },*/
        scaffoldState = scaffoldState,
        backgroundColor = ScreenBackgroundColor,
        modifier = Modifier
//            .background(ScreenBackgroundColor)
            .padding(bottom = bottomPadding)
    ) {

        if (persons.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "No persons in group",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(20.dp),
                    style = MaterialTheme.typography.h5,
                    color = IconGreen,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.no_persons),
                    contentDescription = "no persons",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(300.dp),
                    tint = IconGreen
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            items(persons) { person ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(HalfGray)
                        .clickable {
//                            extNavController.navigate(
//                                Screen.NavPersonInfoScreen.createRoute(person.id)
//                            )
                        },
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = person.displayName,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            ),
                            style = MaterialTheme.typography.h5,
                            color = TextWhite,
                        )
                        Text(
                            "id: ${person.id}",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 10.dp),
                            style = MaterialTheme.typography.h6,
                            color = LightRed,
                        )
                        Text(
                            "Groups id`s: ${person.groups}",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 10.dp),
                            style = MaterialTheme.typography.h6,
                            color = LightGreen2,
                        )
                    }
                }
            }
        }
    }
}