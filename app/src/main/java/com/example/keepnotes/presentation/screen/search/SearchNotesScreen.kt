package com.example.keepnotes.presentation.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepnotes.presentation.component.SearchScreenTopBar
import com.example.keepnotes.presentation.screen.allnotes.NoteCard
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_8dp

@Composable
fun SearchNotesScreen(
    searchNotesViewModel: SearchNotesViewModel = hiltViewModel(),
    navController: NavController,

    ) {
    val context = LocalContext.current
    val allNotes by searchNotesViewModel.searchNotesList.collectAsState()



    Scaffold(
        topBar = {
            SearchScreenTopBar(
                navController,
                onQueryChanged = {
                    searchNotesViewModel.onSearchTextChange(it)
                },
                onClearQuery = {
                    searchNotesViewModel.onSearchTextChange("")
                }
            )
        },
        containerColor = BackgroundColor
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(DIMENS_8dp),
                horizontalArrangement = Arrangement.spacedBy(DIMENS_8dp),

                verticalItemSpacing = DIMENS_8dp
            ) {
                items(allNotes, key = { it.key!! }) { item ->

                    NoteCard(
                        item = item,
                        isSelected = false,
                        onClick = {

                        },
                        onLongClick = {

                        }
                    )
                }
            }
        }
    }
}