package com.shubhamkodes.noter.data

import com.shubhamkodes.noter.presentation.*
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class NoteEntity : RealmObject {
    @PrimaryKey
    var id: Long = Clock.System.now().toEpochMilliseconds()
    var title: String = ""
    var content: String = ""
    var colorHex: Long = 0
    var created: Long = 0
}