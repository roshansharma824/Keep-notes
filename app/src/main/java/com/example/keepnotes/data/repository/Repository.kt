package com.example.keepnotes.data.repository

import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.repository.LocalDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun insertNote(item: RealtimeModelResponse.RealtimeItems) = localDataSource.insertNote(item)

    fun getAllNote() = localDataSource.getAllNote()
    fun getNote(key: String) = localDataSource.getNote(key)
    fun deleteNote(
        key: String
    ) = localDataSource.deleteNote(key = key)

    fun updateNote(
        res: RealtimeModelResponse
    ) = localDataSource.updateNote(res)

}