package com.shubhamkodes.noter.data

import com.shubhamkodes.noter.domain.note.Note
import com.shubhamkodes.noter.domain.time.DateTimeUtil
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun NoteEntity.toNote(): Note {
    return Note(
        id,
        title,
        content,
        colorHex,
        Instant.fromEpochMilliseconds(created)
            .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
    )
}

fun Note.toNoteEntity():NoteEntity{
    return NoteEntity().apply {
        this.id = this@toNoteEntity.id
        this.title=this@toNoteEntity.title
        this.content=this@toNoteEntity.content
        this.colorHex=this@toNoteEntity.colorHex
        this.created= DateTimeUtil.toEpochMillis(this@toNoteEntity.created)
    }
}