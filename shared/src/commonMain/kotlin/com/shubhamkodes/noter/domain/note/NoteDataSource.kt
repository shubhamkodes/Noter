package com.shubhamkodes.noter.domain.note

import com.shubhamkodes.noter.data.NoteEntity
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface NoteDataSource {

    suspend fun insertNote(note: Note)
    suspend fun getNoteById(id:Long):Note?
    suspend fun getAllNotes():List<Note>
     fun observeAllNotes(): Flow<List<Note>>
    suspend fun deleteNoteById(id:Long)

}