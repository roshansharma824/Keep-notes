package com.example.keepnotes.domain.usecase.getnoteusecase

import com.example.keepnotes.data.repository.Repository

class GetNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int) = repository.getNote(id)
}