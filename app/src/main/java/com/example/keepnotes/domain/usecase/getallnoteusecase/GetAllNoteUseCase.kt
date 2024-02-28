package com.example.keepnotes.domain.usecase.getallnoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow


class GetAllNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<List<Note>> = repository.getAllNote()
}