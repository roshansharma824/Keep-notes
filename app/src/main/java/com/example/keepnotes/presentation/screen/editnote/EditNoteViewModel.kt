package com.example.keepnotes.presentation.screen.editnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Define the EditNoteEvent class
sealed class EditNoteEvent {
    data class AddNote(
        val title: String,
        val note: String,
        val timestamp: Long
    ) : EditNoteEvent()

    data class UpdateNote(
        val title: String,
        val note: String,
        val timestamp: Long
    ) : EditNoteEvent()

    data class GetNote(
        val noteId: String
    ) : EditNoteEvent()
}

// Define the UI state class
data class EditNoteUiState(
    val title: String = "",
    val note: String = "",
    val noteId: String = "",
    val isLoading: Boolean = false,
    val timestamp: Long = 0L,
    val error: String = ""
)
data class NoteGetData(
    val title: String = "",
    val note: String = "",
    val noteId: String = "",
    val timestamp: Long = 0L,
)

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditNoteUiState())
    val uiState: StateFlow<EditNoteUiState> = _uiState

    private val _noteData = MutableStateFlow(NoteGetData())
//    val noteData: StateFlow<NoteGetData> = _noteData

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.AddNote -> {
                addNote(
                    title = event.title,
                    note = event.note,
                    timestamp = event.timestamp
                )
            }
            is EditNoteEvent.GetNote -> getNote(
                noteId = event.noteId
            )

            is EditNoteEvent.UpdateNote -> {
                updateNote(
                    title = event.title,
                    note = event.note,
                    timestamp = event.timestamp
                )
            }
        }
    }

    private fun addNote(
        title: String,
        note: String,
        timestamp: Long
    ) = viewModelScope.launch {
        val item = RealtimeModelResponse.RealtimeItems(
            userId = InMemoryCache.userData.userId,
            title = title,
            note = note,
            createdAt = timestamp,
            updatedAt = timestamp
        )

        useCases.addNoteUseCase.invoke(item).collect {
            // Handle the result if needed
        }
    }

    private fun updateNote(
        title: String,
        note: String,
        timestamp: Long
    ) = viewModelScope.launch {
        if (_noteData.value.note == note && _noteData.value.title == title){
            return@launch
        }
        val item = RealtimeModelResponse(
            item = RealtimeModelResponse.RealtimeItems(
                userId = InMemoryCache.userData.userId,
                title = title,
                note = note,
                updatedAt = timestamp
            ),
            key = _uiState.value.noteId
        )
        useCases.updateNoteUseCase.invoke(item).collect {
            // Handle the result if needed
        }
    }

    private fun getNote(
        noteId: String
    ) = viewModelScope.launch {
        useCases.getNoteUseCase.invoke(noteId).collect {
            when (it) {
                is ResultState.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.msg.toString()
                    )
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        title = it.data.item?.title ?: "",
                        note = it.data.item?.note ?: "",
                        noteId = it.data.key ?: "",
                        timestamp = it.data.item?.updatedAt ?: 0L
                    )
                    _noteData.value = _noteData.value.copy(
                        title = it.data.item?.title ?: "",
                        note = it.data.item?.note ?: "",
                        noteId = it.data.key ?: "",
                        timestamp = it.data.item?.updatedAt ?: 0L
                    )
                }
            }
        }
    }
}