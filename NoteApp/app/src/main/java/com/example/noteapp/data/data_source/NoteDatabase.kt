package com.example.noteapp.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteapp.domain.model.Note
import com.example.noteapp.util.DEVLogger

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {

        private const val DATABASE_NAME = "note-db"

        // For Singleton instantiation
        @Volatile private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(context, NoteDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        DEVLogger.d("buildDatabase onCreate()")
                    }
                })
                .build()
        }
    }

}