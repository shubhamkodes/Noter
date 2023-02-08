package com.shubhamkodes.noter.android.note_list

import com.shubhamkodes.noter.domain.note.Note

data class NoteListState(
    val notes:List<Note> = emptyList(),
    val searchText:String ="",
    val isSearchActive:Boolean = false
)