package com.example.noteapp.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.gallery.GalleryActivity
import com.example.noteapp.ui.gl.OpenGLES20Activity
import com.example.noteapp.util.DEVLogger
import kotlinx.coroutines.launch


@Composable
fun WriteScreen(
    onClickWriteBtn: (writeNote: Note) -> Unit,
    onSelectedImg: (selectedImgUri: Uri) -> Unit,
    selectedImgUri: Uri?
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val galleryPickIntent = Intent(LocalContext.current, GalleryActivity::class.java)
    val drawingIntent = Intent(LocalContext.current, OpenGLES20Activity::class.java)
    val photoPickerIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Intent(MediaStore.ACTION_PICK_IMAGES)
    } else {
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }
    }


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
                    DEVLogger.d("[success] result : $uri")
                    // photo picker : content://media/picker/0/com.android.providers.media.photopicker/media/1000000019
                    // action-get-content : content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F46/ORIGINAL/NONE/image%2Fjpeg/975528631
                } ?: run {
                    DEVLogger.d("[error] result : no data")
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                DEVLogger.d("[cancel] result : none")
            }
        }

    val takePhotoFromPhotoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    DEVLogger.d("[success] result : $uri")
                    onSelectedImg(uri)
                } ?: run {
                    DEVLogger.d("[error] result : no data")
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                DEVLogger.d("[cancel] result : none")
            }
        }

    val takeDrawingFromBoardLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { data ->
                    DEVLogger.d("[success] result : $data")
                } ?: run {
                    DEVLogger.d("[error] result : no data")
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                DEVLogger.d("[cancel] result : none")
            }
        }

    var titleValue by remember { mutableStateOf("") }
    var contentValue by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Write") }
            )
        },
        bottomBar = {
            BottomBar(
                onClickWriteBtn = {
                    if (titleValue.isNotEmpty()) {
                        onClickWriteBtn(
                            Note(
                                title = titleValue,
                                content = contentValue
                            )
                        )
                    } else {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Please enter a title.",
                                actionLabel = null
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column {



            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {

                OutlinedTextField(
                    value = titleValue,
                    onValueChange = { titleValue = it },
                    label = { Text("Title") },
                    maxLines = 1,
                    textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contentValue,
                    onValueChange = { contentValue = it },
                    label = { Text("Content") },
                    maxLines = 8,
                    textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Normal)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        takePhotoFromPhotoPicker.launch(photoPickerIntent)
                    },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text("Select Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        takeDrawingFromBoardLauncher.launch(drawingIntent)
                    },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text("Drawing")
                }

                if (selectedImgUri != null) {
                    DEVLogger.d("selectedImgUri != null")
                    Image(
                        painter = rememberImagePainter(
                            data = selectedImgUri,
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    DEVLogger.d("selectedImgUri == null")
                }

            }

        }

    }

}

@Composable
private fun BottomBar(
    onClickWriteBtn: () -> Unit
) {
    Surface(elevation = 8.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onClickWriteBtn,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("Write")
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewWriteScreen() {
//    WriteScreen(onClickWriteBtn = {})
//}