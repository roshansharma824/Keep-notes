package com.example.keepnotes.presentation.screen.allnotes

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.component.BottomBar
import com.example.keepnotes.presentation.component.HomeScreenTopBar
import com.example.keepnotes.presentation.component.SelectedTopBar
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.BottomBarBackgroundColor
import com.example.keepnotes.ui.theme.CardBorder
import com.example.keepnotes.ui.theme.DIMENS_12dp
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_1dp
import com.example.keepnotes.ui.theme.DIMENS_3dp
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.DIMENS_8dp
import com.example.keepnotes.ui.theme.GrayBackground
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.ui.theme.SelectedCardBorder
import com.example.keepnotes.ui.theme.TEXT_SIZE_18sp
import com.example.keepnotes.ui.theme.TextColor
import com.example.keepnotes.ui.theme.UndoTextColor
import com.example.keepnotes.utils.showToast
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.coroutines.launch


@Composable
fun AllNotesScreen(
    openDrawer: () -> Unit,
    navController: NavController,
    allNotesViewModel: AllNotesViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var isInSelectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<String>() }
    val resetSelectionMode: () -> Unit by rememberUpdatedState {
        isInSelectionMode = false
        selectedItems.clear()
    }

    BackHandler(enabled = isInSelectionMode) {
        resetSelectionMode()
    }

    LaunchedEffect(isInSelectionMode, selectedItems.size) {
        if (isInSelectionMode && selectedItems.isEmpty()) {
            isInSelectionMode = false
        }
    }

    val allNotes by allNotesViewModel.allNotesList.collectAsState()

    var changeView by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    actionColor = UndoTextColor,
                    contentColor = TextColor
                )
            }
        },
        topBar = {
            if (isInSelectionMode) {
                SelectedTopBar(
                    onClickAction = resetSelectionMode,
                    onClickMenu = { },
                    onDelete = {
                        allNotesViewModel.deleteNote(selectedItems[0])
                        resetSelectionMode()
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Note Deleted Successfully..",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Long
                            )
                            when (result) {
                                SnackbarResult.ActionPerformed -> Log.d(
                                    "SnackbarResult",
                                    "ActionPerformed"
                                )

                                SnackbarResult.Dismissed -> Log.d("SnackbarResult", "Dismissed")
                            }
                        }
                    },
                    onMakeCopy = {
                        allNotesViewModel.makeCopyNote(selectedItems[0])
                        resetSelectionMode()
                    },
                    selectItemCount = selectedItems.size
                )
            } else {
                HomeScreenTopBar(
                    onClickAction = { openDrawer() },
                    onSearch = { navController.navigate(Screen.Search.route) },
                    onChangeView = { changeView = !changeView }
                )
            }
        },
        backgroundColor = BackgroundColor,
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.EditNote.passNoteId(noteId = "-1")) },
                backgroundColor = BottomBarBackgroundColor,
                contentColor = TextColor,
                shape = RoundedCornerShape(DIMENS_16dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "New Note",
                    modifier = Modifier.size(DIMENS_40dp),
                    tint = GrayBackground
                )
            }
        },
        isFloatingActionButtonDocked = true
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (allNotes.isLoading) {
                ProgressIndicator()
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(if (changeView) 1 else 2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(DIMENS_8dp),
                    horizontalArrangement = Arrangement.spacedBy(DIMENS_8dp),
                    verticalItemSpacing = DIMENS_8dp
                ) {
                    items(allNotes.item, key = { it.key!! }) { item ->
                        val isSelected = selectedItems.contains(item.key)
                        NoteCard(
                            item = item,
                            isSelected = isSelected,
                            onClick = {
                                if (isInSelectionMode) {
                                    if (isSelected) selectedItems.remove(item.key)
                                    else selectedItems.add(item.key!!)
                                } else {
                                    navController.navigate(Screen.EditNote.passNoteId(noteId = item.key!!))
                                }
                            },
                            onLongClick = {
                                if (isInSelectionMode) {
                                    if (isSelected) selectedItems.remove(item.key)
                                    else selectedItems.add(item.key!!)
                                } else {
                                    isInSelectionMode = true
                                    selectedItems.add(item.key!!)
                                }
                            }
                        )
                    }
                }
            }
            if (allNotes.error.isNotEmpty()) {
                context.showToast(allNotes.error, Toast.LENGTH_LONG)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    item: RealtimeModelResponse,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isSelected: Boolean
) {
    val richTextState = remember { RichTextState() }
    val note = remember(item.item?.note) { item.item?.note }

    LaunchedEffect(note) {
        note?.let { richTextState.setHtml(it) }
    }

    Card(
        border = BorderStroke(
            width = if (isSelected) DIMENS_3dp else DIMENS_1dp,
            color = if (isSelected) SelectedCardBorder else CardBorder
        ),
        shape = RoundedCornerShape(size = DIMENS_12dp),
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(DIMENS_16dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BackgroundColor, shape = RoundedCornerShape(size = DIMENS_8dp))
                .padding(DIMENS_12dp)
        ) {
            Text(
                text = item.item?.title.orEmpty(),
                style = TextStyle(
                    fontSize = TEXT_SIZE_18sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left
                )
            )

            RichText(
                state = richTextState,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoteCardPreview() {
//    AllNotesScreen(navController = rememberNavController())
}