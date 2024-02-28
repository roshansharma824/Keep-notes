package com.example.keepnotes.di

import com.example.keepnotes.data.repository.Repository
import com.example.keepnotes.domain.usecase.UseCases
import com.example.keepnotes.domain.usecase.addnoteusecase.AddNoteUseCase
import com.example.keepnotes.domain.usecase.deletenoteusecase.DeleteNoteUseCase
import com.example.keepnotes.domain.usecase.getallnoteusecase.GetAllNoteUseCase
import com.example.keepnotes.domain.usecase.getnoteusecase.GetNoteUseCase
import com.example.keepnotes.domain.usecase.updatenoteusecase.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUseCase(repository: Repository): UseCases {
        return UseCases(
            addNoteUseCase = AddNoteUseCase(repository),
            getAllNoteUseCase = GetAllNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            updateNoteUseCase = UpdateNoteUseCase(repository)
        )
    }
}