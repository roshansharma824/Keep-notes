package com.example.keepnotes.data.repository

import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.repository.LocalDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    suspend fun insertNote(note: Note) = localDataSource.insertNote(note)

    suspend fun updateNote(note: Note) = localDataSource.updateNote(note)

    suspend fun deleteNote(note: Note) = localDataSource.deleteNote(note)

    suspend fun getNote(id: Int) = localDataSource.getNote(id)

    suspend fun getAllNote() = localDataSource.getAllNote()
}