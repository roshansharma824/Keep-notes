package com.example.keepnotes.presentation.screen.checklistnote


import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepnotes.R
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.component.EditNoteBottomBar
import com.example.keepnotes.presentation.component.KeepNoteLinkDialog
import com.example.keepnotes.presentation.component.KeepNotePanel
import com.example.keepnotes.presentation.screen.editnote.EditNoteViewModel
import com.example.keepnotes.presentation.screen.editnote.EditableTextField
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_24dp
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.utils.showToast
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(
    ExperimentalRichTextApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.CheckListNote(
    navController: NavController,
    noteId: String = "-1",
    editNoteViewModel: EditNoteViewModel = hiltViewModel(),
    animatedContentScope: AnimatedContentScope,
){
    var titleInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    var isShowTextEditorPanel by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val richTextState = rememberRichTextState()


    val openLinkDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        richTextState.setConfig(
            linkColor = Color(0xFF1d9bd1),
            linkTextDecoration = TextDecoration.None,
            codeColor = Color(0xFFd7882d),
            codeBackgroundColor = Color.Transparent,
            codeStrokeColor = Color(0xFF494b4d),
        )
    }


    val note by editNoteViewModel.note.collectAsState()

    if (noteId != "-1") {
        LaunchedEffect(Unit) {
            editNoteViewModel.getNote(noteId)
        }
    }

    if (note.error.isNotEmpty()) {
        context.showToast(note.error, Toast.LENGTH_LONG)
    }


    LaunchedEffect(note.item.key) {
        note.item.item?.title?.let {
            titleInput = it
        }
        note.item.item?.note?.let {
            noteInput = it
            richTextState.setHtml(it)
        }
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
            if (isShowTextEditorPanel){
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
            }else{
                EditNoteBottomBar(
                    updatedAt = note.item.item?.updatedAt ?: System.currentTimeMillis(),
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

            if (note.isLoading) {
                ProgressIndicator()
            } else {
                // Editable text
                EditableTextField(text = titleInput, placeholderText = "Title", animatedContentScope) { newText ->
                    titleInput = newText
                }
                VerticalReorderList()

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

            editNoteViewModel.updateNote(richTextState.toHtml())
            editNoteViewModel.updateTitle(titleInput)
            if (titleInput.isNotEmpty() || richTextState.annotatedString.text.isNotEmpty()) {
                if (noteId == "-1") {
                    editNoteViewModel.addNote()
                } else {
                    editNoteViewModel.updateNote()
                }

            }

        }
    }
}



@Composable
fun VerticalReorderList() {
    val size = remember {
        mutableIntStateOf(1)
    }
    val data = remember { mutableStateOf(List(size.intValue) { "item $it" }) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(BackgroundColor)
                ) {
                    CheckBoxNotes()
                }
            }
        }
    }

    IconButton(onClick = {
        size.intValue += 1
        data.value = data.value.toMutableList().apply {
            add("item ${size.intValue}")
        }
    }, modifier = Modifier.fillMaxWidth(0.45f)) {
        Row(verticalAlignment = Alignment.CenterVertically,) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = "icon.name",
                tint = GrayTextColor,
                modifier = Modifier
                    .padding(6.dp)
            )
            Text(text = "List item", color = GrayTextColor)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBoxNotes(){
    val richTextState = rememberRichTextState()
    var checked by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = DIMENS_16dp)
    ) {
        Icon(painter = painterResource(R.drawable.drag), contentDescription ="", tint = GrayTextColor, modifier = Modifier.size(
            DIMENS_24dp) )
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
        RichTextEditor(
            state = richTextState,
            placeholder = {
                Text(
                    text = "Note",
                )
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(color = GrayTextColor),
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

    }
}