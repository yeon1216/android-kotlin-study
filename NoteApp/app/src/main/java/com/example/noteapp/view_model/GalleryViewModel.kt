package com.example.noteapp.view_model

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
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
        fetchImages()
    }

    private fun fetchImages() {
        DEVLogger.d("fetchImages() start")
        val uris = mutableListOf<Uri>()

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
            it.copy(imgUris = uris, isLoading = false)
        }

    }

}

private data class GalleryViewModelState(
    val imgUris: List<Uri>? = null,
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null
) {

    fun toUiState(): GalleryUiState =
        if (imgUris == null) {
            GalleryUiState.NoContents(
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,"")
            )
        } else {
            GalleryUiState.HasContents(
                imgUris = imgUris,
                isLoading = isLoading,
                errorMessage = errorMessage ?: ErrorMessage(0,"")
            )
        }
}


sealed interface GalleryUiState {
    val isLoading: Boolean
    val errorMessage: ErrorMessage

    data class NoContents(
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage
    ): GalleryUiState

    data class HasContents(
        val imgUris: List<Uri>,
        override val isLoading: Boolean,
        override val errorMessage: ErrorMessage
    ): GalleryUiState

}