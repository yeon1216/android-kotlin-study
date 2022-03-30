package com.example.noteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NoteItem(
    note: Note
) {

    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        Modifier
            .padding(8.dp)
            .clickable { }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                format.format(Date(note.date)),
                color = Color.Black,
                style = TextStyle(textDecoration = TextDecoration.None)
            )
            Text(
                note.title,
                color = Color.Black,
                style = TextStyle(textDecoration = TextDecoration.None)
            )
        }
    }

}