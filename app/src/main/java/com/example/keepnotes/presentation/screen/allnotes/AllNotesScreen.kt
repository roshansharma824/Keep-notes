package com.example.keepnotes.presentation.screen.allnotes

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.keepnotes.ui.theme.*
import com.example.keepnotes.utils.showToast
import kotlinx.coroutines.launch


@Composable
fun AllNotesScreen(
    openDrawer: () -> Unit,
    navController: NavController,
    allNotesViewModel: AllNotesViewModel = hiltViewModel(),
) {


    val context = LocalContext.current

    var isInSelectionMode by remember {
        mutableStateOf(false)
    }
    val selectedItems = remember {
        mutableStateListOf<String>()
    }
    val resetSelectionMode = {
        isInSelectionMode = false
        selectedItems.clear()
    }
    BackHandler(
        enabled = isInSelectionMode,
    ) {
        resetSelectionMode()
    }
    LaunchedEffect(
        key1 = isInSelectionMode,
        key2 = selectedItems.size,
    ) {
        if (isInSelectionMode && selectedItems.isEmpty()) {
            isInSelectionMode = false
        }
    }

    val allNotes by allNotesViewModel.allNotesList.collectAsState()

    var changeView by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = { snackbarData: SnackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    actionColor = UndoTextColor,
                    contentColor = TextColor
                )
            })
        },
        topBar = {
            if (isInSelectionMode)
                SelectedTopBar(
                    onClickAction = {
                        resetSelectionMode.invoke()
                    },
                    onClickMenu = {

                    }, onDelete = {
                        allNotesViewModel.deleteNote(selectedItems[0])
                        resetSelectionMode.invoke()
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Note Deleted Successfully..",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Long
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    Log.d("SnackbarResult", "ActionPerformed")
                                }

                                SnackbarResult.Dismissed -> {
                                    Log.d("SnackbarResult", "Dismissed")
                                }
                            }
                        }
                    },
                    onMakeCopy = {
                        allNotesViewModel.makeCopyNote(selectedItems[0])
                        resetSelectionMode.invoke()
                    },
                    selectItemCount = selectedItems.size
                )
            else
                HomeScreenTopBar(
                    onClickAction = { openDrawer.invoke() },
                    onSearch = {
                        navController.navigate(Screen.Search.route)
                    },
                    onChangeView = {
                        changeView = !changeView
                    }
                )
        },
        backgroundColor = BackgroundColor,
        bottomBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .background(
                        color = BottomBarBackgroundColor, shape = RoundedCornerShape(
                            DIMENS_16dp
                        )
                    )
                    .size(DIMENS_64dp)
                    .border(
                        shape = RoundedCornerShape(
                            DIMENS_16dp
                        ), width = DIMENS_8dp, color = BackgroundColor
                    )
                    .clickable {
                        navController.navigate(Screen.EditNote.passNoteId(noteId = "-1"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Note",
                    modifier = Modifier.size(
                        DIMENS_40dp
                    )
                )
            }
        },
        isFloatingActionButtonDocked = true

        ) {


        Column(
            modifier = Modifier.padding(it)
        ) {

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
                                    if (isSelected) {
                                        selectedItems.remove(item.key)
                                    } else {
                                        selectedItems.add(item.key!!)
                                    }
                                } else {
                                    navController.navigate(Screen.EditNote.passNoteId(noteId = "${item.key}"))
                                }
                            },
                            onLongClick = {
                                if (isInSelectionMode) {
                                    if (isSelected) {
                                        selectedItems.remove(item.key)
                                    } else {
                                        selectedItems.add(item.key!!)
                                    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    item: RealtimeModelResponse,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isSelected: Boolean
) {


    Card(
        border = if (isSelected) BorderStroke(
            width = DIMENS_3dp,
            color = SelectedCardBorder
        ) else BorderStroke(width = DIMENS_1dp, color = CardBorder),
        shape = RoundedCornerShape(size = DIMENS_12dp),
        modifier = Modifier.combinedClickable(
            onClick = {
                onClick.invoke()
            },
            onLongClick = {
                onLongClick.invoke()
//                allNotesViewModel.deleteNote("${item.key}")
            },
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(DIMENS_16dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BackgroundColor, shape = RoundedCornerShape(size = DIMENS_8dp))
                .padding(
                    start = DIMENS_12dp,
                    top = DIMENS_12dp,
                    end = DIMENS_12dp,
                    bottom = DIMENS_12dp
                )
        ) {
            Text(
                text = item.item?.title!!,
                style = TextStyle(
                    fontSize = TEXT_SIZE_18sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left
                ),
                textAlign = TextAlign.Left
            )

            Text(
                text = item.item.note!!,
                style = TextStyle(
                    fontSize = TEXT_SIZE_14sp,
                    lineHeight = 20.sp,

                    fontWeight = FontWeight(400),
                    color = GrayTextColor,
                    textAlign = TextAlign.Left

                )
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun NoteCardPreview() {
//    AllNotesScreen(navController = rememberNavController())
}