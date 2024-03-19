package com.example.keepnotes.presentation.screen.note

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.keepnotes.ui.theme.*
import com.example.keepnotes.utils.showToast


@Composable
fun AllNotesScreen(
    navController: NavController,
    allNotesViewModel: AllNotesViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    val allNotes by allNotesViewModel.allNotesList.collectAsState()

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
                NoteCard(item = item, navController = navController, allNotesViewModel)
            }
        }
    }


    if (allNotes.error.isNotEmpty()) {
        context.showToast(allNotes.error,Toast.LENGTH_LONG)
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    item: RealtimeModelResponse,
    navController: NavController,
    allNotesViewModel: AllNotesViewModel
) {


    Card(
        border = BorderStroke(width = DIMENS_1dp, color = CardBorder),
        shape = RoundedCornerShape(size = DIMENS_8dp),
        modifier = Modifier.combinedClickable(
            onClick = {
                navController.navigate(Screen.EditNote.passNoteId(noteId = "${item.key}"))
            },
            onLongClick = {
                Log.d("AllNotesScreen", "onLongClick called")
                allNotesViewModel.deleteNote("${item.key}")
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
    AllNotesScreen(navController = rememberNavController())
}