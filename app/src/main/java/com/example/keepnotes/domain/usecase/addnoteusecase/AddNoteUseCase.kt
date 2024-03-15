package com.example.keepnotes.domain.usecase.addnoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse

class AddNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) = repository.insertNote(note)

    operator fun invoke(item: RealtimeModelResponse.RealtimeItems) = repository.insert(item = item)
}