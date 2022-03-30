package com.example.noteapp.view_model

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val noteRepository: NoteRepository
): AndroidViewModel(application) {

    private val _notes = mutableStateOf(emptyList<Note>())
    val notes: State<List<Note>> = _notes

    init {
        viewModelScope.launch {
            noteRepository.observeNotes()
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun insertNote(title: String, content: String) = viewModelScope.launch {
        noteRepository.insert(Note(title = title, content = content))
    }

    fun setNoteTestData() = viewModelScope.launch {
        noteRepository.insert(Note(title = "test-title 1", content = "test-content 1"))
        noteRepository.insert(Note(title = "test-title 2", content = "test-content 2"))
        noteRepository.insert(Note(title = "test-title 3", content = "test-content 3"))
        noteRepository.insert(Note(title = "test-title 4", content = "test-content 4"))
        noteRepository.insert(Note(title = "test-title 5", content = "test-content 5"))
    }

}