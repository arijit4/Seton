package com.arijit4.nothing.notes.di

import android.content.Context
import androidx.room.Room
import com.arijit4.nothing.notes.db.NotesDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NotesDB::class.java,
        "notes.db"
    ).build()

    @Provides
    @Singleton
    fun provideNotesDao(db: NotesDB) = db.dao
}