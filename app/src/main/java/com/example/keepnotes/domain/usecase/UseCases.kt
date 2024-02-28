package com.example.keepnotes.domain.usecase

import com.example.keepnotes.domain.usecase.addnoteusecase.AddNoteUseCase
import com.example.keepnotes.domain.usecase.deletenoteusecase.DeleteNoteUseCase
import com.example.keepnotes.domain.usecase.getallnoteusecase.GetAllNoteUseCase
import com.example.keepnotes.domain.usecase.getnoteusecase.GetNoteUseCase
import com.example.keepnotes.domain.usecase.updatenoteusecase.UpdateNoteUseCase

data class UseCases(
    val addNoteUseCase: AddNoteUseCase,
    val getAllNoteUseCase: GetAllNoteUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase
)
