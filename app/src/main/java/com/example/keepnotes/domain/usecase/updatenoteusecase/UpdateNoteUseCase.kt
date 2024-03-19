package com.example.keepnotes.domain.usecase.updatenoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse

class UpdateNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(items: RealtimeModelResponse) = repository.updateNote(items)
}