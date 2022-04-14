package com.example.noteapp.ui.write

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun WriteScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write") }
            )
        },
    ) {

    }

}