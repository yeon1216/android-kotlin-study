package com.example.noteapp.ui.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.noteapp.view_model.GalleryUiState
import com.example.noteapp.view_model.GalleryViewModel

@Composable
fun GalleryRoute(
    galleryViewModel: GalleryViewModel, galleryActivity: GalleryActivity
) {
    val galleryUiState: GalleryUiState by galleryViewModel.uiState.collectAsState()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) galleryViewModel.fetchImages()
        }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if(galleryUiState is GalleryUiState.NoPermission &&
                    galleryViewModel.isPermission(galleryActivity.applicationContext)) {
                    galleryViewModel.fetchImages()
                }
            }
            else -> {}
        }
    }

    GalleryRoute(
        galleryUiState = galleryUiState,
        requestPermission = {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    galleryActivity,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                moveToSettingActivity(galleryActivity = galleryActivity)
            }
        },
        onSelectedImg = { imgIndex -> galleryViewModel.onSelectedImg(imgIndex) },
        onErrorDismiss = { errorId -> galleryViewModel.errorShown(errorId) }
    )

}

fun moveToSettingActivity(galleryActivity: GalleryActivity) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", galleryActivity.packageName, null))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    galleryActivity.startActivity(intent)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GalleryRoute(
    galleryUiState: GalleryUiState,
    requestPermission: () -> Unit,
    onSelectedImg: (imgIndex: Int) -> Unit,
    onErrorDismiss: (Long) -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Gallery") }
            )
        }
    ) {
        when(getGalleryScreenType(galleryUiState)) {
            GalleryScreenType.ImgList -> {
                check(galleryUiState is GalleryUiState.HasContents)
                GalleryGridScreen(
                    imgUris = galleryUiState.imgUris,
                    selectedImgIndex = galleryUiState.selectedImgIndexList,
                    onSelectedImg = onSelectedImg
                )
            }
            GalleryScreenType.NoPermission -> {
                NoPermissionScreen {
                    requestPermission()
                }
            }
            GalleryScreenType.NoImg -> {
                // TODO
            }
        }

        if(galleryUiState.errorMessage.id != 0L) {
            val errorMessage = remember(galleryUiState) { galleryUiState.errorMessage }
            val onErrorDismissState by rememberUpdatedState(onErrorDismiss)
            LaunchedEffect(errorMessage.message, scaffoldState) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = errorMessage.message,
                    actionLabel = null
                )
                onErrorDismissState(errorMessage.id)
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
    is GalleryUiState.NoPermission -> {
        GalleryScreenType.NoPermission
    }
    is GalleryUiState.HasContents -> {
        GalleryScreenType.ImgList
    }
    is GalleryUiState.NoContents -> {
        GalleryScreenType.NoImg
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}