package com.example.noteapp.data.respository

import android.app.Application
import androidx.room.Room
import com.example.noteapp.data.data_source.NoteDao
import com.example.noteapp.data.data_source.NoteDatabase
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return noteDao.observeNotes()
    }

    override suspend fun get(uid: Int): Note {
        return noteDao.get(uid)
    }

    override suspend fun getAll(): List<Note> {
        return noteDao.notes()
    }

    override suspend fun insert(note: Note) {
        return noteDao.insert(note)
    }

    override suspend fun update(note: Note) {
        return noteDao.update(note)
    }

    override suspend fun delete(note: Note) {
        return noteDao.delete(note)
    }

    override suspend fun deleteAll() {
        return noteDao.deleteAll()
    }
}