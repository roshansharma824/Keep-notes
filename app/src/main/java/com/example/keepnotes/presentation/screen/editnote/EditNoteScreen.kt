package com.example.keepnotes.presentation.screen.editnote


import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.presentation.screen.RealtimeViewModel
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.utils.canGoBack


@Composable
fun EditNoteScreen(
    navController: NavController,
    noteId: String = "-1",
    editNoteViewModel: EditNoteViewModel = hiltViewModel(),
    realtimeViewModel: RealtimeViewModel = hiltViewModel()
) {
    var titleInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    val isDialog = remember { mutableStateOf(false) }



    val note = realtimeViewModel.note.collectAsState()

    if (noteId != "-1") {
        LaunchedEffect(Unit){
            realtimeViewModel.getNote(noteId)
        }
    }

    note.value.item.item?.title?.let {
        titleInput = it
    }
    note.value.item.item?.note?.let{
         noteInput = it
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
                realtimeViewModel.updateTitle( newText)
            }
            // Editable text
            EditableTextField(text = noteInput, placeholderText = "Note") { newText ->
                noteInput = newText
                realtimeViewModel.updateNote( newText)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (titleInput.isNotEmpty() || noteInput.isNotEmpty()) {
                if (noteId == "-1") {

                    val newNote =
                        Note(id = Math.random().toInt(), title = titleInput, note = noteInput)
                    editNoteViewModel.addNote(newNote)
                    realtimeViewModel.insert(
                        RealtimeModelResponse.RealtimeItems(
                            userId = InMemoryCache.userData.userId,
                            title = titleInput,
                            note = noteInput
                        )
                    )
                } else {
                    realtimeViewModel.update()
                }

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