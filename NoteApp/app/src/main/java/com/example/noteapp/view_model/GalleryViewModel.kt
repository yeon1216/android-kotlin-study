package com.example.noteapp.view_model

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.util.ErrorMessage
import kotlinx.coroutines.flow.*

class GalleryViewModel(
    application: Application
): AndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(GalleryViewModelState(
        isLoading = true
    ))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        if (isPermission(applicationContext = application.applicationContext)) {
            fetchImages()
        } else {
            viewModelState.update {
                it.copy(isLoading = false, isPermission = false)
            }
        }
    }

    fun isPermission(applicationContext: Context): Boolean =
        ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED

    fun fetchImages() {
        DEVLogger.d("fetchImages() start")
        val uris: MutableList<Uri> = mutableListOf<Uri>()

        getApplication<Application>().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                DEVLogger.d("fetchImages() contentUri : $contentUri")
                uris.add(contentUri)
            }
        }

        viewModelState.update {
            it.copy(imgUris = uris, isLoading = false, isPermission = true)
        }

    }

    fun onSelectedImg(imgIndex: Int) {
        val tempSelectedImgIndexList: MutableList<Int> = mutableListOf()
        viewModelState.value.selectedImgIndexList?.forEach {
            tempSelectedImgIndexList.add(it)
        }
        if (tempSelectedImgIndexList.contains(imgIndex)) {
            tempSelectedImgIndexList.remove(imgIndex)
        } else {
            tempSelectedImgIndexList.add(imgIndex)
        }
        if (tempSelectedImgIndexList.size > 3) {
            viewModelState.update {
                it.copy(isLoading = false, isPermission = true, errorMessage = ErrorMessage(1, "You can select up to three images."))
            }
        } else {
            viewModelState.update {
                it.copy(selectedImgIndexList = tempSelectedImgIndexList, isLoading = false, isPermission = true)
            }
        }

    }

    // TODO update logic
    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            if (currentUiState.errorMessage?.id == errorId) {
                currentUiState.copy(errorMessage = ErrorMessage(0, ""))
            } else {
                currentUiState.copy(errorMessage = ErrorMessage(0, ""))
            }
        }
    }


}

private data class GalleryViewModelState(
    val imgUris: List<Uri>? = null,
    val selectedImgIndexList: List<Int>? = null,
    val isLoading: Boolean = false,
    val isPermission: Boolean = false,
    val errorMessage: ErrorMessage? = null
) {

    fun toUiState(): GalleryUiState =
        if (!isPermission) {
            GalleryUiState.NoPermission(
                isLoading = isLoading,
                isPermission = isPermission,
                errorMessage = errorMessage ?: ErrorMessage(0,"")
            )
        } else if (imgUris == null) {
            GalleryUiState.NoContents(
                isLoading = isLoading,
                isPermission = isPermission,
                errorMessage = errorMessage ?: ErrorMessage(0L,"")
            )
        } else {
            GalleryUiState.HasContents(
                imgUris = imgUris,
                selectedImgIndexList = selectedImgIndexList ?: listOf(),
                isLoading = isLoading,
                isPermission = isPermission,
                errorMessage = errorMessage ?: ErrorMessage(0L,"")
            )
        }
}


sealed interface GalleryUiState {
    val isLoading: Boolean
    val isPermission: Boolean
    val errorMessage: ErrorMessage

    data class NoPermission(
        override val isLoading: Boolean,
        override val isPermission: Boolean,
        override val errorMessage: ErrorMessage
    ): GalleryUiState

    data class NoContents(
        override val isLoading: Boolean,
        override val isPermission: Boolean,
        override val errorMessage: ErrorMessage
    ): GalleryUiState

    data class HasContents(
        val imgUris: List<Uri>,
        val selectedImgIndexList: List<Int>,
        override val isLoading: Boolean,
        override val isPermission: Boolean,
        override val errorMessage: ErrorMessage
    ): GalleryUiState

}