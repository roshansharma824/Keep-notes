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




}