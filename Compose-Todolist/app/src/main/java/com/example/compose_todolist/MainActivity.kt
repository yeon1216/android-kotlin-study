package com.example.compose_todolist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_todolist.domain.util.TodoAndroidViewModelFactory
import com.example.compose_todolist.ui.main.MainScreen
import com.example.compose_todolist.ui.main.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel(
                factory = TodoAndroidViewModelFactory(application)
            )
            MainScreen(viewModel = mainViewModel)
        }
    }
}
