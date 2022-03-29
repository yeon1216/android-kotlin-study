package com.example.noteapp.data.data_source

import androidx.room.*
import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note ORDER BY date DESC")
    fun notes(): Flow<List<Note>>

    @Query("SELECT * FROM Note Where uid = :uid")
    suspend fun get(uid: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

}