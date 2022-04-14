package com.example.noteapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.components.NoteItem
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.view_model.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onClickFloatingBtn: () -> Unit,
    onClickItem: (noteId: Int) -> Unit
) {

    val notes: List<Note> = when(uiState) {
        is HomeUiState.HasNotes -> uiState.notes.allNotes
        is HomeUiState.NoNotes -> emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    DEVLogger.d("FloatingActionButton onClick")
                    onClickFloatingBtn()
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "add note"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                items(notes) { item ->

                    Column(
                        Modifier.clickable {
                            onClickItem(item.uid)
                        }
                    ) {
                        NoteItem(note = item)
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.Black, thickness = 1.dp)

                    }

                }
            }
        }
    }
}