package com.shubhamkodes.noter.android.note_list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamkodes.noter.data.RealmNoteDataSource
import com.shubhamkodes.noter.domain.NoteRepository
import com.shubhamkodes.noter.domain.note.Note
import com.shubhamkodes.noter.domain.note.NoteDataSource
import com.shubhamkodes.noter.domain.note.SearchNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class NoteListViewModel @Inject constructor(
    val noteDataSource: NoteRepository.Companion,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchNotes = SearchNotes()

    private val notes = savedStateHandle.getStateFlow("notes", emptyList<Note>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    init {

        observeNotes()
    }


    private fun observeNotes() {

        viewModelScope.launch {
            noteDataSource.observeAllNotes().collectLatest {
                savedStateHandle["notes"] = it
                Log.e("ViewModel","Observed : $it")
            }
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch() {
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if (!isSearchActive.value) {
            onSearchTextChange("")
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
        }
    }
}