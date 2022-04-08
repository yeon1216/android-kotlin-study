package com.example.noteapp.data.respository

import com.example.noteapp.data.data_source.NoteDao
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.noteapp.data.Result

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return noteDao.observeNotes()
    }

    override suspend fun get(uid: Int): Note {
        return noteDao.get(uid)
    }

    override suspend fun getAll(): Result<List<Note>> {
        return withContext(Dispatchers.IO) {
            try {
                val notes = noteDao.getAll()
                Result.Success(notes)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
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