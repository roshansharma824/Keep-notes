package com.example.keepnotes.presentation.screen.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    private val useCases: UseCases
):ViewModel() {

    private val _allNotesList = MutableStateFlow<List<Note>>(emptyList())
    val allNotesList = _allNotesList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getAllNoteUseCase.invoke().collect{ notes ->
//                _allNotesList.value = notes
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            useCases.deleteNoteUseCase.invoke(note)
        }
    }
}