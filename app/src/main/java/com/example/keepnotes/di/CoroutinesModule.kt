package com.example.keepnotes.di

import com.example.keepnotes.utils.CoroutinesDispatchers
import com.example.keepnotes.utils.CoroutinesDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {
    @Binds
    @Singleton
    fun bindCoroutinesDispatchers(
        impl: CoroutinesDispatchersImpl
    ): CoroutinesDispatchers
}