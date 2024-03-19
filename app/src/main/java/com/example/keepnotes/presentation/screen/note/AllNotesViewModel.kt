package com.example.keepnotes.presentation.screen.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.ItemState
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.ResultState
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

    private val _allNotesList = MutableStateFlow(ItemState())
    val allNotesList = _allNotesList.asStateFlow()

    init {

        viewModelScope.launch {
            useCases.getAllNoteUseCase.invoke().collect {
                when (it) {
                    is ResultState.Failure -> {
                        _allNotesList.value = ItemState(
                            error = it.msg.toString()
                        )
                    }

                    ResultState.Loading -> {
                        _allNotesList.value = ItemState(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _allNotesList.value = ItemState(
                            item = it.data
                        )
                    }
                }
            }
        }

    }




    fun deleteNote(key:String) = viewModelScope.launch {
        useCases.deleteNoteUseCase.invoke(key)
    }
}