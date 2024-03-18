package com.example.keepnotes.presentation.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.domain.model.ItemState
import com.example.keepnotes.domain.model.NoteState
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import com.example.keepnotes.domain.usecase.addnoteusecase.AddNoteUseCase
import com.example.keepnotes.domain.usecase.deletenoteusecase.DeleteNoteUseCase
import com.example.keepnotes.domain.usecase.getallnoteusecase.GetAllNoteUseCase
import com.example.keepnotes.domain.usecase.updatenoteusecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {

    private val _res = MutableStateFlow(ItemState())
    val res = _res.asStateFlow()

    private val _note = MutableStateFlow(NoteState())
    val note = _note.asStateFlow()

    private var noteInput by mutableStateOf("")
    private var title by mutableStateOf("")
    private var noteId by mutableStateOf("")
    private var userId by mutableStateOf("")

    fun updateNote(input: String) {
        noteInput = input
    }
    fun updateTitle(input: String) {
        title = input
    }

    init {

        viewModelScope.launch {
            useCases.getAllNoteUseCase.invoke().collect {
                when (it) {
                    is ResultState.Failure -> {
                        _res.value = ItemState(
                            error = it.msg.toString()
                        )
                    }

                    ResultState.Loading -> {
                        _res.value = ItemState(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _res.value = ItemState(
                            item = it.data
                        )
                    }
                }
            }
        }

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
                        title = data
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


    fun insert(item: RealtimeModelResponse.RealtimeItems) = viewModelScope.launch {

        useCases.addNoteUseCase.invoke(item).collect{

        }
    }

    fun delete(key:String) = viewModelScope.launch {
        useCases.deleteNoteUseCase.invoke(key)
    }

    fun update() = viewModelScope.launch {

        val item = RealtimeModelResponse(
            item = RealtimeModelResponse.RealtimeItems(
                userId = InMemoryCache.userData.userId,
                title = title,
                note = noteInput
            ),
            key = noteId
        )
        useCases.updateNoteUseCase.invoke(item).collect{
        }
    }
}