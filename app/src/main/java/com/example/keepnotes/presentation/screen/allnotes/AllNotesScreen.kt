package com.example.keepnotes.presentation.screen.allnotes

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.component.DropdownMenuOptions
import com.example.keepnotes.presentation.component.HomeScreenTopBar
import com.example.keepnotes.presentation.component.SelectedTopBar
import com.example.keepnotes.ui.theme.*
import com.example.keepnotes.utils.showToast


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



    Scaffold(
        topBar = {
            if (isInSelectionMode)
                SelectedTopBar(
                    onClickAction = resetSelectionMode, onClickMenu = {

                    }, onDelete = {
                        allNotesViewModel.deleteNote(selectedItems[0])
                        resetSelectionMode.invoke()
                    },
                    selectItemCount = selectedItems.size
                )
            else
                HomeScreenTopBar(
                    onClickAction = { openDrawer }
                )
        },
        containerColor = BackgroundColor
    ) {


        Column(
            modifier = Modifier.padding(it)
        ) {

            if (allNotes.isLoading) {
                ProgressIndicator()
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(DIMENS_8dp),
                    horizontalArrangement = Arrangement.spacedBy(DIMENS_8dp),

                    verticalItemSpacing = DIMENS_8dp
                ) {
                    items(allNotes.item, key = { it.key!! }) { item ->
                        Log.d("AllNotes", "${item.key}")
                        val isSelected = selectedItems.contains(item.key)
                        NoteCard(
                            item = item,
                            navController = navController,
                            allNotesViewModel = allNotesViewModel,
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

//            DropdownMenuOptions(expanded = expanded, onDismiss = {expanded = false})


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
    navController: NavController,
    allNotesViewModel: AllNotesViewModel,
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