package com.example.noteapp.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.view_model.MainViewModel
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val application: Application,
    private val noteRepository: NoteRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, noteRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}