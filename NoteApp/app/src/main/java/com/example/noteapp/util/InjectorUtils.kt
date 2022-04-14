package com.example.noteapp.util

import android.app.Application
import com.example.noteapp.data.data_source.NoteDatabase
import com.example.noteapp.data.respository.NoteRepositoryImpl
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.view_model.HomeViewModelFactory

object InjectorUtils {

    private fun getNoteRepository(application: Application): NoteRepository {
        return NoteRepositoryImpl(
            noteDao = NoteDatabase.getInstance(application.applicationContext).noteDao()
        )
    }

    fun providerNoteViewModelFactory(application: Application): HomeViewModelFactory {
        return HomeViewModelFactory(
            application = application, noteRepository = getNoteRepository(application = application)
        )
    }

}