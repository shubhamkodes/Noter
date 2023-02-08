package com.shubhamkodes.noter.domain

import com.shubhamkodes.noter.data.RealmNoteDataSource
import com.shubhamkodes.noter.data.wrap
import com.shubhamkodes.noter.domain.note.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository private constructor() {
    companion object {

        private val noteDataSource by lazy {
            RealmNoteDataSource()
        }

        suspend fun deleteAll() = noteDataSource.deleteAllNotes()
        suspend fun insertNote(note: Note) = noteDataSource.insertNote(note)
        suspend fun getNoteById(id: Long) = noteDataSource.getNoteById(id)

        fun getAllNotes() = noteDataSource.observeAllNotes().wrap()
        fun observeAllNotes() = noteDataSource.observeAllNotes()
        suspend fun deleteNoteById(id: Long) = noteDataSource.deleteNoteById(id)

    }
}