package com.example.keepnotes.di

import com.example.keepnotes.utils.Constants.USERS
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun providesRealtimeDb(): DatabaseReference {
        val firebaseRef = Firebase.database.reference.child(USERS)
        firebaseRef.keepSynced(true)
        return firebaseRef
    }

}