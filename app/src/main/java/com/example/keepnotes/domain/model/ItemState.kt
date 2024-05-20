package com.example.keepnotes.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ItemState(
    var item: List<RealtimeModelResponse> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class NoteState(
    val item: RealtimeModelResponse = RealtimeModelResponse(null),
    val error: String = "",
    val isLoading: Boolean = false
)
