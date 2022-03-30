package com.example.noteapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.noteapp.ui.components.NoteItem
import com.example.noteapp.view_model.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note")}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            Divider()

            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),   // 상하 좌우 패딩
                verticalArrangement = Arrangement.spacedBy(16.dp),  // 아이템간의 패딩
            ) {
                items(viewModel.notes.value) { item ->

                    Column {
                        NoteItem(note = item)
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.Black, thickness = 1.dp)
                    }

                }
            }
        }
    }
}