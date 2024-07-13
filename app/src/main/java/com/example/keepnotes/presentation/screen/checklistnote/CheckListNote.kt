package com.example.keepnotes.presentation.screen.checklistnote


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepnotes.R
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.component.EditNoteBottomBar
import com.example.keepnotes.presentation.component.KeepNotePanel
import com.example.keepnotes.presentation.screen.editnote.EditNoteEvent
import com.example.keepnotes.presentation.screen.editnote.EditNoteViewModel
import com.example.keepnotes.presentation.screen.editnote.EditableTextField
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckListNote(
    navController: NavController,
    noteId: String = "-1",
    editNoteViewModel: EditNoteViewModel = hiltViewModel(),
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
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back arrow",
                    tint = GrayTextColor,
                    modifier = Modifier
                        .size(
                            DIMENS_40dp
                        )
                        .padding(start = DIMENS_16dp)
                        .clickable {
                            navController.popBackStack()

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
            horizontalAlignment = Alignment.Start,
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
                // Editable text
                EditableTextField(
                    initialText = titleTextState,
                    placeholderText = "Title"
                ) { newText ->
//                    titleInput = newText
                }
                VerticalReorderList(editNoteViewModel)

            }


        }
    }
}

data class CheckNote(
    val index: Int = 0,
    val content: String = "",
    val isChecked: Boolean = false
)

@Composable
fun VerticalReorderList(editNoteViewModel: EditNoteViewModel) {
    val data = editNoteViewModel.data
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        editNoteViewModel.moveCheckNote(from.index, to.index)
    })

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data, { it.index }) { item ->
            ReorderableItem(state, key = item.index) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(Color.White)
                ) {
                    CheckBoxNotes(item, editNoteViewModel)
                }
            }
        }
    }

    IconButton(
        onClick = { editNoteViewModel.addCheckNote() },
        modifier = Modifier.fillMaxWidth(0.45f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = "Add item",
                tint = Color.Gray,
                modifier = Modifier.padding(6.dp)
            )
            Text(text = "List item", color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBoxNotes(item: CheckNote, editNoteViewModel: EditNoteViewModel) {
    val richTextState = rememberRichTextState()
    var checked by remember { mutableStateOf(item.isChecked) }
    var content by remember { mutableStateOf(item.content) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = DIMENS_16dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.drag),
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                editNoteViewModel.updateCheckNoteChecked(item.index, it)
            }
        )
        RichTextEditor(
            state = richTextState,
            placeholder = {
                Text(
                    text = "Note",
                )
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(color = Color.Gray),
            colors = RichTextEditorDefaults.richTextEditorColors(
                textColor = Color(0xFFCBCCCD),
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.White.copy(alpha = .6f),
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Listen for changes in RichTextEditor and update ViewModel state
        DisposableEffect(Unit) {
            onDispose {
                editNoteViewModel.updateCheckNoteContent(item.index, richTextState.toHtml())
            }
        }
    }
}