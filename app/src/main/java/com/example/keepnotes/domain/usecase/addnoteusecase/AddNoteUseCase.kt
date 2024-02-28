package com.example.keepnotes.domain.usecase.addnoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note

class AddNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) = repository.insertNote(note)
}