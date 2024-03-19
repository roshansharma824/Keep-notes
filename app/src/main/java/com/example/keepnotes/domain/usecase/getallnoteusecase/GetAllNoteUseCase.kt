package com.example.keepnotes.domain.usecase.getallnoteusecase

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import kotlinx.coroutines.flow.Flow


class GetAllNoteUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<ResultState<List<RealtimeModelResponse>>> = repository.getAllNote()
}