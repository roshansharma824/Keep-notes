package com.example.keepnotes.domain.model

data class ItemState(
    val item: List<RealtimeModelResponse> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class NoteState(
    val item: RealtimeModelResponse = RealtimeModelResponse(null),
    val error: String = "",
    val isLoading: Boolean = false
)
