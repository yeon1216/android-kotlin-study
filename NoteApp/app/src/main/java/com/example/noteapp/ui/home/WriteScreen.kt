package com.example.noteapp.ui.home

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.launch


@Composable
fun WriteScreen(
    onClickWriteBtn: (writeNote: Note) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val takePhotoFromAlbumIntentGetContent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    val takePhotoFromAlbumIntentActionPick =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "$uri 선택",
                            actionLabel = null
                        )
                    }
                } ?: run {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "에러 발생",
                            actionLabel = null
                        )
                    }
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "취소",
                        actionLabel = null
                    )
                }
            }
        }

    Scaffold(
        scaffoldState = scaffoldState,
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

                Button(
                    onClick = {
                        takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntentActionPick)
                    },
                    colors = ButtonDefaults.textButtonColors(),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text("Select Image")
                }

            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
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