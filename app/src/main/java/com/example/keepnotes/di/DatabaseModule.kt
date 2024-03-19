package com.example.keepnotes.di

import android.content.Context
import androidx.room.Room
import com.example.keepnotes.data.local.NoteDatabase
import com.example.keepnotes.data.repository.LocalDataSourceImpl
import com.example.keepnotes.domain.repository.LocalDataSource
import com.example.keepnotes.utils.Constants.NOTE_DATABASE
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NOTE_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: NoteDatabase,
        realtimeDb: DatabaseReference
    ): LocalDataSource {
        return LocalDataSourceImpl(database, realtimeDb)
    }
}