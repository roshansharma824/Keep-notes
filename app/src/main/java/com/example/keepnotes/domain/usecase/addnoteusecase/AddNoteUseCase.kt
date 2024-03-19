package com.example.keepnotes.domain.usecase.addnoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse

class AddNoteUseCase(
    private val repository: Repository
) {
    operator fun invoke(item: RealtimeModelResponse.RealtimeItems) = repository.insertNote(item = item)
}