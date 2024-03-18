package com.example.keepnotes.data.repository

import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.repository.LocalDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    suspend fun insertNote(note: Note) = localDataSource.insertNote(note)

    suspend fun updateNote(note: Note) = localDataSource.updateNote(note)

    suspend fun deleteNote(note: Note) = localDataSource.deleteNote(note)

    fun getNote(id: Int) = localDataSource.getNote(id)

    fun getAllNote() = localDataSource.getAllNote()

    fun insert(item: RealtimeModelResponse.RealtimeItems) = localDataSource.insert(item)

    fun getItems() = localDataSource.getItems()
    fun getItem(key: String) = localDataSource.getItem(key)
    fun delete(
        key: String
    ) = localDataSource.delete(key = key)

    fun update(
        res: RealtimeModelResponse
    ) = localDataSource.update(res)

}