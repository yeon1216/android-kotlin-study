package com.example.noteapp.domain.repository

import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow
import com.example.noteapp.data.Result

interface NoteRepository {
    fun observeNotes(): Flow<List<Note>>
    suspend fun get(uid: Int): Note
    suspend fun getAll(): Result<List<Note>>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    suspend fun deleteAll()
}