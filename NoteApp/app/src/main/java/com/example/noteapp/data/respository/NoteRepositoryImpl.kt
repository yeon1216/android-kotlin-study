package com.example.noteapp.data.respository

import android.app.Application
import androidx.room.Room
import com.example.noteapp.data.data_source.NoteDatabase
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(application: Application): NoteRepository {

    private val db = Room.databaseBuilder(
        application,
        NoteDatabase::class.java,
        "note-db"
    ).build()

    override fun observeNotes(): Flow<List<Note>> {
        return db.noteDao().notes()
    }

    override suspend fun get(uid: Int): Note {
        return db.noteDao().get(uid)
    }

    override suspend fun insert(note: Note) {
        return db.noteDao().insert(note)
    }

    override suspend fun update(note: Note) {
        return db.noteDao().update(note)
    }

    override suspend fun delete(note: Note) {
        return db.noteDao().delete(note)
    }

}