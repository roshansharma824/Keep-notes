package com.example.keepnotes.presentation.screen.editnote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.domain.model.NoteState
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import com.example.keepnotes.presentation.screen.checklistnote.CheckNote
import dagger.hilt.android.lifecycle.HiltViewModel
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
            note = noteInput,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        useCases.addNoteUseCase.invoke(item).collect{

        }
    }

    fun updateNote() = viewModelScope.launch {

        val item = RealtimeModelResponse(
            item = RealtimeModelResponse.RealtimeItems(
                userId = InMemoryCache.userData.userId,
                title = titleInput,
                note = noteInput,
                updatedAt = System.currentTimeMillis()
            ),
            key = noteId
        )
        useCases.updateNoteUseCase.invoke(item).collect{
        }
    }

    private val _data = mutableStateListOf<CheckNote>()
    val data: SnapshotStateList<CheckNote> get() = _data

    fun addCheckNote() {
        _data.add(CheckNote(index = _data.size + 1))
    }

    fun moveCheckNote(fromIndex: Int, toIndex: Int) {
        if (fromIndex in 0 until _data.size && toIndex in 0 until _data.size) {
            _data.add(toIndex, _data.removeAt(fromIndex))
        }
    }

    fun updateCheckNoteContent(index: Int, content: String) {
        if (index in 0 until _data.size) {
            _data[index] = _data[index].copy(content = content)
        }
    }

    fun updateCheckNoteChecked(index: Int, isChecked: Boolean) {
        if (index in 0 until _data.size) {
            _data[index] = _data[index].copy(isChecked = isChecked)
        }
    }
}