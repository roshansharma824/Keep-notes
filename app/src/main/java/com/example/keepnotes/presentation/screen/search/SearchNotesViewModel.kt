package com.example.keepnotes.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.ListNoteState
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchNotesViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    private val _allNotesList = MutableStateFlow<ListNoteState>(ListNoteState.START)
    private val allNotesList = MutableStateFlow<List<RealtimeModelResponse>>(emptyList())

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    private val searchText = _searchText.asStateFlow()

    init {

        viewModelScope.launch {
            useCases.getAllNoteUseCase.invoke().collect {
                when (it) {
                    is ResultState.Failure -> {
                        _allNotesList.value = ListNoteState.FAILURE(
                            message = it.msg.toString()
                        )
                    }

                    ResultState.Loading -> {
                        _allNotesList.value = ListNoteState.LOADING
                    }

                    is ResultState.Success -> {
                        _allNotesList.value = ListNoteState.SUCCESS(
                            items = it.data
                        )
                        allNotesList.value = it.data
                    }
                }
            }
        }

    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    val searchNotesList  = searchText
        .combine(allNotesList) { text, itemState ->
            if (text.isBlank()) {
                allNotesList.value
            }
            itemState.filter { realtimeModelResponse->// filter and return a list of countries based on the text the user typed
                realtimeModelResponse.item?.title?.uppercase()?.contains(text.trim().uppercase()) == true ||  realtimeModelResponse.item?.note?.uppercase()?.contains(text.trim().uppercase()) == true
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = allNotesList.value
        )

}

