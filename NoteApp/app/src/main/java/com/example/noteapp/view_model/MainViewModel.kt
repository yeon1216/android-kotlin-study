package com.example.noteapp.view_model

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.model.Notes
import com.example.noteapp.domain.model.Notes.Companion.toNotes
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.util.ErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val noteRepository: NoteRepository
): AndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(MainViewModelState(
        isLoading = true
    ))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshNotes()
    }

    fun refreshNotes() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val notes: Notes = noteRepository.getAll().toNotes()
            viewModelState.update { it.copy(notes = notes, isLoading = false) }
        }
    }

    fun insertNote(title: String, content: String) = viewModelScope.launch {
        noteRepository.insert(Note(title = title, content = content))
    }

    fun getNote(noteId: Int) = viewModelScope.launch {
        noteRepository.get(noteId)
    }

    fun setNoteTestData() = viewModelScope.launch {
        noteRepository.insert(Note(title = "test-title 1", content = "test-content 1"))
        noteRepository.insert(Note(title = "test-title 2", content = "test-content 2"))
        noteRepository.insert(Note(title = "test-title 3", content = "test-content 3"))
        noteRepository.insert(Note(title = "test-title 4", content = "test-content 4"))
        noteRepository.insert(Note(title = "test-title 5", content = "test-content 5"))
    }

}

private data class MainViewModelState(
    val notes: Notes? = null,
    val selectedNoteId: Int? = 0, // TODO back selectedPostId in a SavedStateHandle
    val isNoteOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null
) {

    fun toUiState(): MainUiState =
        if (notes == null) {
            MainUiState.NoNotes(
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,0)
            )
        } else {
            MainUiState.HasNotes(
                notes = notes,
                // Determine the selected post. This will be the post the user last selected.
                // If there is none (or that post isn't in the current feed), default to the
                // highlighted post
                selectedNote = notes.allNotes.find {
                    it.uid == selectedNoteId
                } ?: Note(0,"",""),
                isNoteOpen = isNoteOpen,
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,0)
            )
        }
}


sealed interface MainUiState {
    val isLoading: Boolean
    val errorMessage: ErrorMessage

    data class NoNotes(
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage
    ): MainUiState

    data class HasNotes(
        val notes: Notes,
        val selectedNote: Note,
        val isNoteOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage
    ): MainUiState

}