package com.example.keepnotes.presentation.screen.editnote


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.utils.DummyData
import com.example.keepnotes.utils.canGoBack


@Composable
fun EditNoteScreen(
    navController: NavController,
    noteId: Int? = -1
) {
    var titleInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }

    val note: Note?
    if (noteId != null && noteId != -1) {
        val listNote = DummyData.items.filter { note ->
            note.id == noteId
        }
        note = listNote[0]
        titleInput = note.title
        noteInput = note.note

    } else {
        note = Note(Math.random().toInt(), "", "")
    }

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = BackgroundColor) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "back arrow",
                    tint = GrayTextColor,
                    modifier = Modifier
                        .size(
                            DIMENS_40dp
                        )
                        .clickable {
                            if (navController.canGoBack) {
                                navController.popBackStack()
                            }

                        }
                )
            }
        },
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {


            // Editable text
            EditableTextField(text = titleInput, placeholderText = "Title") { newText ->
                titleInput = newText
                note.title = newText
            }
            // Editable text
            EditableTextField(text = noteInput, placeholderText = "Note") { newText ->
                noteInput = newText
                note.note = newText
            }
        }
    }


}

@Composable
fun EditableTextField(
    text: String,
    placeholderText: String,
    onTextChanged: (String) -> Unit
) {
    var isKeyboardVisible by remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = if (placeholderText == "Title")
                    MaterialTheme.typography.headlineSmall.copy(color = GrayTextColor.copy(alpha = 0.5f))
                else
                    MaterialTheme.typography.titleMedium.copy(color = GrayTextColor.copy(alpha = 0.5f)),
            )
        },
        textStyle = if (placeholderText == "Title") MaterialTheme.typography.headlineSmall.copy(
            color = GrayTextColor
        ) else MaterialTheme.typography.titleMedium.copy(color = GrayTextColor),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isKeyboardVisible = it.isFocused },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = BackgroundColor,
            focusedBorderColor = BackgroundColor,
            unfocusedBorderColor = BackgroundColor,
            focusedLabelColor = BackgroundColor,
            cursorColor = GrayTextColor,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                isKeyboardVisible = false
            }
        )
    )

    DisposableEffect(Unit) {
        onDispose {
            // Cleanup, e.g., close keyboard when the composable is disposed
            if (isKeyboardVisible) {
                // Close keyboard
                isKeyboardVisible = false
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditNoteScreenPreview() {
    EditNoteScreen(navController = rememberNavController())
}