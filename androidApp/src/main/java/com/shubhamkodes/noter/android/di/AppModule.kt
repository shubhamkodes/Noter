package com.shubhamkodes.noter.android.di

import com.shubhamkodes.noter.domain.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataSource():NoteRepository.Companion{
        return NoteRepository
    }

}