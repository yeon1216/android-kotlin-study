package com.example.noteapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import java.util.*


@Composable
fun WriteScreen(
    onClickWriteBtn: (writeNote: Note) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write") }
            )
        },
    ) {
        Column {

            var titleValue by remember { mutableStateOf("") }
            var contentValue by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {

                OutlinedTextField(
                    value = titleValue,
                    onValueChange = { titleValue = it },
                    label = { Text("Title") },
                    maxLines = 1,
                    textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(20.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contentValue,
                    onValueChange = { contentValue = it },
                    label = { Text("Content") },
                    maxLines = 5,
                    textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))



            }

            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = {
                        onClickWriteBtn(
                            Note(
                                title = titleValue,
                                content = contentValue
                            )
                        )
                    },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text("Write")
                }
            }
        }

    }

}

@Preview
@Composable
fun PreviewWriteScreen() {
    WriteScreen(onClickWriteBtn = {})
}