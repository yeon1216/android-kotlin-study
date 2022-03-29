package com.example.noteapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "date")
    val date: Long = Calendar.getInstance().timeInMillis
)
