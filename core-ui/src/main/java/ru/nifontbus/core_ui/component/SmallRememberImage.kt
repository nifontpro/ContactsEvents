package ru.nifontbus.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun SmallRememberImage(
    personId: Long,
    modifier: Modifier = Modifier,
    getImage: suspend (id: Long) -> ImageBitmap?
) {
    var img by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(personId) {
        img = getImage(personId)
    }

    img?.let {
        Image(
            bitmap = it,
            contentDescription = "Photo",
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }
}