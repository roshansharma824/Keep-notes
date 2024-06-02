package com.example.keepnotes.presentation.screen.editnote


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.component.EditNoteBottomBar
import com.example.keepnotes.presentation.component.KeepNoteLinkDialog
import com.example.keepnotes.presentation.component.KeepNotePanel
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.utils.canGoBack
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    navController: NavController,
    noteId: String = "-1",
    editNoteViewModel: EditNoteViewModel = hiltViewModel()
) {
    val uiState by editNoteViewModel.uiState.collectAsState()

    val richTextState = rememberRichTextState()
    val titleTextState = remember { mutableStateOf(uiState.title) }
    val openLinkDialog = remember { mutableStateOf(false) }
    var isShowTextEditorPanel by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        if (noteId != "-1") {
            editNoteViewModel.onEvent(EditNoteEvent.GetNote(noteId))
        }
    }

    LaunchedEffect(uiState.note) {
        titleTextState.value = uiState.title
        richTextState.setHtml(uiState.note)
    }

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = BackgroundColor) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "back arrow",
                    tint = GrayTextColor,
                    modifier = Modifier
                        .size(DIMENS_40dp)
                        .clickable {
                            if (navController.canGoBack) {
                                navController.popBackStack()
                            }
                        }
                )
            }
        },
        bottomBar = {
            if (isShowTextEditorPanel) {
                KeepNotePanel(
                    state = richTextState,
                    openLinkDialog = openLinkDialog,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp),
                    onCloseEditor = {
                        isShowTextEditorPanel = !isShowTextEditorPanel
                    }
                )
            } else {
                EditNoteBottomBar(
                    updatedAt = System.currentTimeMillis(),
                    isShowTextEditorPanel = {
                        isShowTextEditorPanel = !isShowTextEditorPanel
                    }
                )
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(it)
        ) {
            if (uiState.isLoading) {
                ProgressIndicator()
            } else {
                EditableTextField(
                    initialText = titleTextState,
                    placeholderText = "Title"
                ) { newText ->
//                    titleTextState.value = newText
                }

                RichTextEditor(
                    state = richTextState,
                    placeholder = {
                        Text(text = "Note")
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(color = GrayTextColor),
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        textColor = Color(0xFFCBCCCD),
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        placeholderColor = Color.White.copy(alpha = .6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (openLinkDialog.value)
                Dialog(
                    onDismissRequest = {
                        openLinkDialog.value = false
                    }
                ) {
                    KeepNoteLinkDialog(
                        state = richTextState,
                        openLinkDialog = openLinkDialog
                    )
                }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val timestamp = System.currentTimeMillis()
            if (titleTextState.value.isNotEmpty() || richTextState.annotatedString.text.isNotEmpty()) {
                if (noteId == "-1") {
                    editNoteViewModel.onEvent(
                        EditNoteEvent.AddNote(
                            title = titleTextState.value,
                            note = richTextState.toHtml(),
                            timestamp = timestamp
                        )
                    )
                } else {
                    editNoteViewModel.onEvent(
                        EditNoteEvent.UpdateNote(
                            title = titleTextState.value,
                            note = richTextState.toHtml(),
                            timestamp = timestamp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EditableTextField(
    initialText: MutableState<String>,
    placeholderText: String,
    onTextChanged: (String) -> Unit
) {
//    var text by remember { mutableStateOf(initialText) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = initialText.value,
        onValueChange = {
            initialText.value = it
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
            .focusRequester(focusRequester)
            .onFocusChanged {
                isKeyboardVisible = it.isFocused
                if (it.isFocused) {
                    keyboardController?.show()
                }
            },
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