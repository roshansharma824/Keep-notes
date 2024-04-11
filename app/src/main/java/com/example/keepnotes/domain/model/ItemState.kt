package com.example.keepnotes.domain.model

sealed class ListNoteState {
    data object START : ListNoteState()
    data object LOADING : ListNoteState()
    data class SUCCESS(val items: List<RealtimeModelResponse>) : ListNoteState()
    data class FAILURE(val message: String) : ListNoteState()
}

sealed class NoteState {
    data object START : NoteState()
    data object LOADING : NoteState()
    data class SUCCESS(val items: RealtimeModelResponse) : NoteState()
    data class FAILURE(val message: String) : NoteState()
}

