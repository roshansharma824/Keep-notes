package com.example.keepnotes.presentation.screen.editnote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.presentation.component.BottomBar
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_32dp
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.ui.theme.TEXT_SIZE_14sp
import com.example.keepnotes.ui.theme.TEXT_SIZE_18sp
import com.example.keepnotes.utils.DummyData


@Composable
fun EditNoteScreen(
    navController: NavController,
    noteId: Int? = -1
) {


    val titleInput = remember { mutableStateOf("") }
    val noteInput = remember { mutableStateOf("") }
    if (noteId != null && noteId != -1) {
        val note = DummyData.items.filter { note ->
            note.id == noteId
        }
        titleInput.value = note[0].title
        noteInput.value = note[0].note

    } else {
//        titleInput.value = "noteId"
    }

    androidx.compose.material.Scaffold(
        topBar = {
            TopAppBar(backgroundColor = BackgroundColor) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "New Note",
                    tint = GrayTextColor,
                    modifier = Modifier
                        .size(
                            DIMENS_40dp
                        )
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }
        },
//        bottomBar = {
//            Surface(
//                elevation = DIMENS_32dp,
//                shape = RectangleShape
//            ) {
//                Row {
//
//                }
//            }
//        },
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = BackgroundColor)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = titleInput.value.ifEmpty { "Title" },
                style = TextStyle(
                    fontSize = TEXT_SIZE_18sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIMENS_16dp)
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(color = Color.Green, width = 2.dp),
                value = titleInput.value,
                onValueChange = { titleInput.value = it },
                decorationBox = {},
                textStyle = TextStyle(
                    fontSize = TEXT_SIZE_18sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left
                ),
            )

            Text(
                text = noteInput.value.ifEmpty { "Note" },
                style = TextStyle(
                    fontSize = TEXT_SIZE_14sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIMENS_16dp)
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(color = Color.Green, width = 2.dp),
                value = noteInput.value,
                onValueChange = { noteInput.value = it },
                decorationBox = {},
                textStyle = TextStyle(
                    fontSize = TEXT_SIZE_18sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left
                ),
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun EditNoteScreenPreview() {
    EditNoteScreen(navController = rememberNavController())
}