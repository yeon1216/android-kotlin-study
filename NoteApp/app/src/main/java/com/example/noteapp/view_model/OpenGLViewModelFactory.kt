package com.example.noteapp.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.noteapp.domain.repository.NoteRepository
import java.lang.IllegalArgumentException

class OpenGLViewModelFactory(
    private val application: Application
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(OpenGLViewModel::class.java)) {
            return OpenGLViewModel(application) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}