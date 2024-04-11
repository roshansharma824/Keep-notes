package com.example.keepnotes.presentation.screen.allnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.ListNoteState
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllNotesViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _allNotesList = MutableStateFlow<ListNoteState>(ListNoteState.START)
    val allNotesList = _allNotesList.asStateFlow()

    init {

        viewModelScope.launch {
            useCases.getAllNoteUseCase.invoke().collect {
                when (it) {
                    is ResultState.Failure -> {
                        _allNotesList.value = ListNoteState.FAILURE(message = it.msg.toString())
                    }

                    ResultState.Loading -> {
                        _allNotesList.value = ListNoteState.LOADING
                    }

                    is ResultState.Success -> {
                        _allNotesList.value = ListNoteState.SUCCESS(
                            items = it.data
                        )
                    }
                }
            }
        }

    }


    fun deleteNote(key: String) = viewModelScope.launch {
        useCases.deleteNoteUseCase.invoke(key).collect {

        }
    }

    fun makeCopyNote(key: String) = viewModelScope.launch {

        (allNotesList.value as ListNoteState.SUCCESS).items.forEach { it ->
            if (it.key == key) {
                it.item?.let { it1 ->
                    useCases.addNoteUseCase.invoke(it1).collect {
//                        when(it){
//                            is ResultState.Failure -> State.FAILURE(it.msg.toString())
//                            ResultState.Loading -> _allNotesList.value = State.LOADING
//                            is ResultState.Success -> TODO()
//                        }
                    }
                }
            }
        }


    }
}