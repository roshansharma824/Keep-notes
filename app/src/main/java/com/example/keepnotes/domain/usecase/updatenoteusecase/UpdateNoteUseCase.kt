package com.example.keepnotes.domain.usecase.updatenoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note

class UpdateNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}