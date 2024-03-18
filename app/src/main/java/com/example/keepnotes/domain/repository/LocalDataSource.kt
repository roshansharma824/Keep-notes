package com.example.keepnotes.domain.repository

import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNote(id: Int): Flow<Note>
    fun getAllNote(): Flow<List<Note>>


    fun insert(
        item: RealtimeModelResponse.RealtimeItems
    ): Flow<ResultState<String>>

    fun getItems(): Flow<ResultState<List<RealtimeModelResponse>>>

    fun getItem(key: String): Flow<ResultState<RealtimeModelResponse>>

    fun delete(
        key: String
    ): Flow<ResultState<String>>

    fun update(
        res: RealtimeModelResponse
    ): Flow<ResultState<String>>

}