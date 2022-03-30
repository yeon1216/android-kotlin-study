package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.respository.NoteRepositoryImpl
import com.example.noteapp.domain.util.MainViewModelFactory
import com.example.noteapp.ui.home.HomeScreen
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.util.InjectorUtils
import com.example.noteapp.view_model.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MainViewModel = viewModel(
                factory = InjectorUtils.providerNoteViewModelFactory(application)
            )

            viewModel.setNoteTestData()

            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}