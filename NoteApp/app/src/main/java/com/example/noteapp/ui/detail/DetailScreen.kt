package com.example.noteapp.ui.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.example.noteapp.view_model.MainViewModel

@Composable
fun DetailScreen(
    viewModel: MainViewModel,
    noteId: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail $noteId") }
            )
        },
    ) {

    }
}