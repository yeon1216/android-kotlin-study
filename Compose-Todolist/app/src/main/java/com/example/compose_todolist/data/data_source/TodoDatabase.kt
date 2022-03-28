package com.example.compose_todolist.data.data_source

import androidx.room.Database
import com.example.compose_todolist.domain.model.Todo


@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : androidx.room.RoomDatabase() {
    abstract fun todoDao(): TodoDao
}