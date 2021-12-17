package ru.nifontbus.contactsevents.presentation.persons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.nifontbus.contactsevents.R
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.utils.Search
import ru.nifontbus.contactsevents.presentation.navigation.BottomNavItem
import ru.nifontbus.contactsevents.presentation.navigation.Screen
import ru.nifontbus.contactsevents.ui.theme.*

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PersonsScreen(
    extNavController: NavController,
    bottomPadding: Dp,
) {
    val viewModel: PersonsViewModel = hiltViewModel()
    val scaffoldState = rememberScaffoldState()
    val persons = viewModel.persons.value
    val grouped = persons.groupBy { it.displayName[0] }

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
        topBar = { SearchView(state = viewModel.searchState) },
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .padding(bottom = bottomPadding)
    ) {

        LaunchedEffect(key1 = viewModel.searchState.value) {
            viewModel.updatePerson()
        }
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
            grouped.forEach { (initial, contactsForInitial) ->
                stickyHeader {
                    Text(
                        "$initial ",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.secondary)
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

                items(contactsForInitial) { person ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colors.surface)
                            .clickable {
                                extNavController.navigate(
                                    Screen.NavPersonInfoScreen.createRoute(person.id)
                                )
                            },
                    ) {
                        PersonCard(person, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonCard(
    person: Person,
    viewModel: PersonsViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Column(
            modifier = Modifier.weight(6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = Search.colorSubstring(
                    person.displayName,
                    viewModel.searchState.value,
                    MaterialTheme.colors.onBackground, Color.Red
                ),
                style = MaterialTheme.typography.h6,
            )
        } // Col

        SmallRememberImage(
            person,
            Modifier
                .weight(1f)
                .size(50.dp)
                .clip(RoundedCornerShape(10))
        ) { viewModel.getPhotoById(person.id) }
    } // Row
}

@Composable
fun SmallRememberImage(
    person: Person,
    modifier: Modifier = Modifier,
    getImage: () -> ImageBitmap?
) {
    var img by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(person.id) {
        img = getImage()
    }

    img?.let {
        Image(
            bitmap = it,
            contentDescription = "Photo",
            modifier = modifier
        )
    }
}

@Composable
fun SearchView(state: MutableState<String>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
//        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
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