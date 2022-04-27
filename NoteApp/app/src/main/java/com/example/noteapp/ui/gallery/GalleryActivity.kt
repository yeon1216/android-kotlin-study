package com.example.noteapp.ui.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.util.InjectorUtils
import com.example.noteapp.view_model.GalleryViewModel

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val galleryViewModel: GalleryViewModel = viewModel(
                factory = InjectorUtils.providerGalleryViewModelFactory(application)
            )
            GalleryRoute(galleryViewModel = galleryViewModel)
        }
    }
}