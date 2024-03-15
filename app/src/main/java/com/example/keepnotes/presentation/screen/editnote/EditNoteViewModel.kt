package com.example.keepnotes.presentation.screen.editnote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _getNote = MutableStateFlow<Note>(Note(-1,"",""))
    val getNote = _getNote.asStateFlow()
    private var note by mutableStateOf("")
    private var title by mutableStateOf("")

    fun updateNote(input: String) {
        note = input
    }
    fun updateTitle(input: String) {
        title = input
    }



    fun getNote(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getNoteUseCase.invoke(id).collect { notes ->
                _getNote.value = notes
            }
        }
    }


    fun addNote(note: Note) = viewModelScope.launch {
        useCases.addNoteUseCase.invoke(note)
    }

    fun updateNote() = viewModelScope.launch {
        val updateNote = Note(id = getNote.value.id, title = title.ifBlank { getNote.value.title }, note = note.ifBlank { getNote.value.note })
        useCases.updateNoteUseCase.invoke(updateNote)
    }
}