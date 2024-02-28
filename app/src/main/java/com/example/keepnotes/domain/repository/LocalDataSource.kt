package com.example.keepnotes.domain.repository

import com.example.keepnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNote(id: Int): Flow<Note>
    fun getAllNote(): Flow<List<Note>>

}