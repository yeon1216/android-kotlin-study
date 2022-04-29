package com.example.noteapp.ui.gallery

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.noteapp.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryGridScreen(
    imgUris: List<Uri>,
    selectedImgIndex: List<Int>,
    onSelectedImg: (imgIndex: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp
    val itemHeight: Dp = (screenWidth - 12.dp) / 3

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(imgUris.size) { imgIndex ->

            Box(
                modifier = Modifier.height(itemHeight).padding(2.dp)
                    .clickable { onSelectedImg(imgIndex) }
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = imgUris[imgIndex],
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_radio_button_unchecked_24),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                )
                if(selectedImgIndex.contains(imgIndex)) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 6.dp, end = 8.dp),
                        text = "✔")
                }
            }

        }
    }

}

@Preview
@Composable
fun PreviewGalleryComponent() {
//    GalleryGridScreen(imgUris = listOf())
}