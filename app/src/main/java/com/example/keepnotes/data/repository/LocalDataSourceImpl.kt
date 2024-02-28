package com.example.keepnotes.data.repository

import com.example.keepnotes.data.local.NoteDatabase
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.repository.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    noteDatabase: NoteDatabase
): LocalDataSource {

    private val noteDao = noteDatabase.noteDao()
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note = note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getNote(id: Int) = noteDao.getNote(id)

    override fun getAllNote() = noteDao.getAllNote()

}