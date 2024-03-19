package com.example.keepnotes.presentation.screen.editnote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.NoteState
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
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

    private val _note = MutableStateFlow(NoteState())
    val note = _note.asStateFlow()

    private var noteInput by mutableStateOf("")
    private var titleInput by mutableStateOf("")
    private var noteId by mutableStateOf("")
    private var userId by mutableStateOf("")

    fun updateNote(input: String) {
        noteInput = input
    }
    fun updateTitle(input: String) {
        titleInput = input
    }



    fun getNote(key: String) = viewModelScope.launch {
        useCases.getNoteUseCase.invoke(key).collect{
            when (it) {
                is ResultState.Failure -> {
                    _note.value = NoteState(
                        error = it.msg.toString()
                    )
                }

                is ResultState.Loading -> {
                    _note.value = NoteState(
                        isLoading = true
                    )
                }

                is ResultState.Success -> {
                    _note.value = NoteState(
                        item = it.data
                    )
                    it.data.item?.note?.let { data->
                        noteInput = data
                    }
                    it.data.item?.title?.let { data->
                        titleInput = data
                    }
                    it.data.item?.userId?.let { data->
                        userId = data
                    }
                    it.data.key?.let { data->
                        noteId = data
                    }
                }
            }
        }
    }

    fun addNote() = viewModelScope.launch {

        val item = RealtimeModelResponse.RealtimeItems(
            userId = InMemoryCache.userData.userId,
            title = titleInput,
            note = noteInput
        )

        useCases.addNoteUseCase.invoke(item).collect{

        }
    }

    fun updateNote() = viewModelScope.launch {

        val item = RealtimeModelResponse(
            item = RealtimeModelResponse.RealtimeItems(
                userId = InMemoryCache.userData.userId,
                title = titleInput,
                note = noteInput
            ),
            key = noteId
        )
        useCases.updateNoteUseCase.invoke(item).collect{
        }
    }
}