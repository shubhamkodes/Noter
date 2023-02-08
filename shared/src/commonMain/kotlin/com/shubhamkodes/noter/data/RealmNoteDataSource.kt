package com.shubhamkodes.noter.data

import com.shubhamkodes.noter.domain.note.Note
import com.shubhamkodes.noter.domain.note.NoteDataSource
import com.shubhamkodes.noter.domain.time.DateTimeUtil
import io.realm.kotlin.LogConfiguration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class RealmNoteDataSource(
    private val db: Realm = Realm.open(
        RealmConfiguration.create(
            schema = setOf(
                NoteEntity::class
            )
        )
    )
) : NoteDataSource {
    override suspend fun insertNote(note: Note) {
        println("DataSource: Inserting $note")
        val id = note.id;
        db.writeBlocking {
            val noteEntity = this.query<NoteEntity>("id == ${id}").first().find()
            if(noteEntity!=null){
                noteEntity.created=DateTimeUtil.toEpochMillis(note.created)
                noteEntity.title = note.title
                noteEntity.content = note.content
            }else{
                this.copyToRealm(note.toNoteEntity())
            }
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return db.query(NoteEntity::class, "id == $id").first().find()?.toNote()
    }

    override suspend fun getAllNotes(): List<Note> {
        return db.query<NoteEntity>().find().map { it.toNote() }
    }

    override  fun observeAllNotes(): Flow<List<Note>> {
        return db.query<NoteEntity>().asFlow()
            .map { it.list.map { noteEntity -> noteEntity.toNote() } }
    }

    override suspend fun deleteNoteById(id: Long) {
        try {

            db.write {
                val note = this.query(NoteEntity::class, "id == $id").first().find()
                note?.let {
                    delete(note)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteAllNotes() {
        db.write {
            deleteAll()
        }
    }



}