package com.shubhamkodes.noter.android.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamkodes.noter.domain.NoteRepository
import com.shubhamkodes.noter.domain.note.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository.Companion,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow("title", "")
    private val isNoteTitleFocused = savedStateHandle.getStateFlow("isNoteTitleFocused", false)
    private val noteContent = savedStateHandle.getStateFlow("noteContent", "")
    private val isNotContentFocused = savedStateHandle.getStateFlow("isNoteContentFocused", false)
    private val noteColor = savedStateHandle.getStateFlow("noteColor", Note.generateRandomColor())

    val state = combine(
        noteTitle,
        isNoteTitleFocused,
        noteContent,
        isNotContentFocused,
        noteColor
    ) { noteTitle, isNoteTitleFocused, noteContent, isNoteContentFocused, noteColor ->
        NoteDetailState(
            noteTitle = noteTitle,
            isNoteTitleHintVisible =  noteTitle.isEmpty() && !isNoteTitleFocused,
            noteContent = noteContent,
            isNoteContentHintVisible = noteContent.isEmpty() && !isNoteContentFocused,
            noteColor = noteColor
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved

    private var existingNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId")?.let {
            if (it == -1L) {
                return@let
            }

            this.existingNoteId = it
            viewModelScope.launch {
                noteRepository.getNoteById(it)?.let { note ->
                    savedStateHandle["title"] = note.title
                    savedStateHandle["noteContent"] = note.content
                    savedStateHandle["noteColor"] = note.colorHex
                }
            }
        }
    }


    fun onNoteTitleChanged(text:String){
        savedStateHandle["title"] = text
    }

    fun onNoteContentChanged(text:String){
        savedStateHandle["noteContent"] = text
    }

    fun onNoteTitleFocusedChanged(isFocused:Boolean){
        savedStateHandle["isNoteTitleFocused"] = isFocused
    }

    fun onNoteContentFocusedChanged(isFocused:Boolean){
        savedStateHandle["isNoteContentFocused"] = isFocused
    }

    fun saveNote(){
        viewModelScope.launch {
            val note = Note(title = noteTitle.value, content = noteContent.value, colorHex = noteColor.value)
            noteRepository.insertNote(if (existingNoteId!=null ) note.copy(id = existingNoteId!!) else note)
            _hasNoteBeenSaved.emit(true)
        }
    }
}