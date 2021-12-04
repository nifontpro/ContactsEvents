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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.utils.Search
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
    val persons = viewModel.persons.value

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
        topBar = { SearchView(state = viewModel.searchState) { viewModel.refresh() } },
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
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
//                .simpleVerticalScrollbar(listState),

//            state = listState,

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
                            text = Search.colorSubstring(
                                person.displayName, viewModel.searchState.value,
                                MaterialTheme.colors.onBackground, Color.Red
                            ),
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            ),
                            style = MaterialTheme.typography.h5,
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
                            color = PrimaryDarkColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(state: MutableState<String>, onUpdate: () -> Unit) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            onUpdate()
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != "") {
                IconButton(
                    onClick = {
                        // Remove text from TextField when you press the 'X' icon
                        state.value = ""
                        onUpdate()
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
/*        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.colorPrimary),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )*/
    )
}