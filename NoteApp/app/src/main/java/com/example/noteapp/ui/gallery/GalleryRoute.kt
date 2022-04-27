package com.example.noteapp.ui.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.noteapp.view_model.GalleryUiState
import com.example.noteapp.view_model.GalleryViewModel

@Composable
fun GalleryRoute(
    galleryViewModel: GalleryViewModel
) {
    val galleryUiState: GalleryUiState by galleryViewModel.uiState.collectAsState()
    GalleryRoute(
        galleryUiState = galleryUiState,
    )
}

@Composable
fun GalleryRoute(
    galleryUiState: GalleryUiState,
) {
    when(getGalleryScreenType(galleryUiState)) {
        GalleryScreenType.ImgList -> {
            check(galleryUiState is GalleryUiState.HasContents)
            GalleryGridScreen(imgUris = galleryUiState.imgUris)
        }
    }
}

private enum class GalleryScreenType {
    ImgList,
}

@Composable
private fun getGalleryScreenType(
    uiState: GalleryUiState
): GalleryScreenType = when (uiState) {
    is GalleryUiState.HasContents -> {
        GalleryScreenType.ImgList
    }
    is GalleryUiState.NoContents -> {
        GalleryScreenType.ImgList
    }
}
