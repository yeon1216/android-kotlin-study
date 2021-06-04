package com.example.calculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator.data.dao.HistoryDao
import com.example.calculator.data.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}