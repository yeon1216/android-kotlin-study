package com.example.noteapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.successOr
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.model.Notes
import com.example.noteapp.domain.model.Notes.Companion.toNotes
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.util.ErrorMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.noteapp.data.Result
class HomeViewModel(
    application: Application,
    private val noteRepository: NoteRepository
): AndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(HomeViewModelState(
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

    fun refreshNotes() = viewModelScope.launch {
        viewModelState.update { it.copy(isLoading = true) }
        val notesDeferred = async { noteRepository.getAll() }
        val notes: Notes = notesDeferred.await().successOr(emptyList()).toNotes()
        viewModelState.update {
            it.copy(notes = notes, isLoading = false)
        }
    }

    fun refreshNotes2() = viewModelScope.launch {
        viewModelState.update { it.copy(isLoading = true) }
        val result: Result<List<Note>> = noteRepository.getAll()
        viewModelState.update {
            when(result) {
                is Result.Success -> it.copy(notes = result.data.toNotes(), isLoading = false)
                is Result.Error -> {
                    val errorMessage = ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        message = result.exception.message.toString()
                    )
                    it.copy(errorMessage = errorMessage, isLoading = false)
                }
            }
        }
    }

    fun insertNote(insertNote: Note) = viewModelScope.launch {
        noteRepository.insert(note = insertNote)
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

    fun interactWithNoteDetail(noteId: Int) {
        viewModelState.update {
            it.copy(
                selectedNoteId = noteId,
                isNoteOpen = true
            )
        }
    }

    fun interactWithNoteList() {
        viewModelState.update {
            it.copy(
                isNoteOpen = false,
                isWriteOpen = false
            )
        }
    }

    fun interactWithNoteWrite() {
        viewModelState.update {
            it.copy(
                isWriteOpen = true
            )
        }
    }

}

private data class HomeViewModelState(
    val notes: Notes? = null,
    val selectedNoteId: Int? = 0, // TODO back selectedPostId in a SavedStateHandle
    val isNoteOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null,
    val isWriteOpen: Boolean = false
) {

    fun toUiState(): HomeUiState =
        if (notes == null) {
            HomeUiState.NoNotes(
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,""),
                isWriteOpen = isWriteOpen
            )
        } else {
            HomeUiState.HasNotes(
                notes = notes,
                // Determine the selected post. This will be the post the user last selected.
                // If there is none (or that post isn't in the current feed), default to the
                // highlighted post
                selectedNote = notes.allNotes.find {
                    it.uid == selectedNoteId
                } ?: Note("",""),
                isNoteOpen = isNoteOpen,
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,""),
                isWriteOpen = isWriteOpen
            )
        }
}


sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessage: ErrorMessage
    val isWriteOpen: Boolean

    data class NoNotes(
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage,
        override val isWriteOpen: Boolean
    ): HomeUiState

    data class HasNotes(
        val notes: Notes,
        val selectedNote: Note,
        val isNoteOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage,
        override val isWriteOpen: Boolean
    ): HomeUiState

}