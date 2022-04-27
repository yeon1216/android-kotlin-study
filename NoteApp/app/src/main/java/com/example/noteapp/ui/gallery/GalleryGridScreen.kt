package com.example.noteapp.ui.gallery

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.noteapp.util.DEVLogger


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryGridScreen(
    imgUris: List<Uri>
) {
    DEVLogger.d("GalleryGridScreen start ${imgUris.size}")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gallery")}
            )
        }
    ) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp
        val cardHeight: Dp = (screenWidth - 12.dp) / 3

        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(imgUris.size) { imgIndex ->

                Card(
                    modifier = Modifier.height(cardHeight).padding(2.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = imgUris[imgIndex],
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }

}

@Preview
@Composable
fun PreviewGalleryComponent() {
    GalleryGridScreen(imgUris = listOf())
}