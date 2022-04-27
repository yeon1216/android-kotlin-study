package com.example.noteapp.ui.gallery

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
        onGrantedPermission = { galleryViewModel.fetchImages() }
    )
}

@Composable
fun GalleryRoute(
    galleryUiState: GalleryUiState,
    onGrantedPermission: () -> Unit
) {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) onGrantedPermission()
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gallery") }
            )
        }
    ) {
        when(getGalleryScreenType(galleryUiState)) {
            GalleryScreenType.ImgList -> {
                check(galleryUiState is GalleryUiState.HasContents)
                GalleryGridScreen(imgUris = galleryUiState.imgUris)
            }
            GalleryScreenType.NoPermission -> {
                NoPermissionScreen() {
                    launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            GalleryScreenType.NoImg -> {
                // TODO
            }
        }
    }
}

private enum class GalleryScreenType {
    NoImg,
    NoPermission,
    ImgList
}

@Composable
private fun getGalleryScreenType(
    uiState: GalleryUiState
): GalleryScreenType = when (uiState) {
    is GalleryUiState.HasContents -> {
        GalleryScreenType.ImgList
    }
    is GalleryUiState.NoContents -> {
        if(uiState.isPermission) {
            GalleryScreenType.NoImg
        } else {
            GalleryScreenType.NoPermission
        }

    }
}
