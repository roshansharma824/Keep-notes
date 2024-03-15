package com.example.keepnotes.presentation.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.domain.model.ItemState
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.usecase.UseCases
import com.example.keepnotes.domain.usecase.addnoteusecase.AddNoteUseCase
import com.example.keepnotes.domain.usecase.deletenoteusecase.DeleteNoteUseCase
import com.example.keepnotes.domain.usecase.getallnoteusecase.GetAllNoteUseCase
import com.example.keepnotes.domain.usecase.updatenoteusecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {

    private val _res: MutableState<ItemState> = mutableStateOf(ItemState())
    val res: State<ItemState> = _res

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


    fun insert(item: RealtimeModelResponse.RealtimeItems) = viewModelScope.launch {

        useCases.addNoteUseCase.invoke(item).collect{

        }
    }

    fun delete(key:String) = viewModelScope.launch {
        useCases.deleteNoteUseCase.invoke(key)
    }

    fun update(item: RealtimeModelResponse) = viewModelScope.launch {
        useCases.updateNoteUseCase.invoke(item)
    }
}