package ru.nifontbus.core_ui.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

class Search {

    companion object {
        fun colorSubstring(
            string: String,
            searchString: String,
            mainColor: Color,
            searchColor: Color
        ): AnnotatedString {
            val searchIdx = if (searchString.isEmpty()) -1 to -1
            else string.containsIndex(searchString)
            if (searchIdx.first == -1) {
                return buildAnnotatedString {
                    withStyle(style = SpanStyle(mainColor)) {
                        append(string)
                    }
                }
            }
            return buildAnnotatedString {
                if (searchIdx.first > 0) {
                    withStyle(style = SpanStyle(mainColor)) {
                        append(string.substring(0, searchIdx.first))
                    }
                }

                withStyle(style = SpanStyle(searchColor)) {
                    append(string.substring(searchIdx.first, searchIdx.second + 1))
                }

                if (searchIdx.second < string.length - 1) {
                    withStyle(style = SpanStyle(mainColor)) {
                        append(string.substring(searchIdx.second + 1, string.length))
                    }
                }
            }
        }
    }
}

fun String.containsIndex(find: String): Pair<Int, Int> {
    val idx1 = this.indexOf(find, ignoreCase = true)
    val idx2 = if (idx1 != -1) idx1 + find.length - 1 else -1
    return idx1 to idx2
}