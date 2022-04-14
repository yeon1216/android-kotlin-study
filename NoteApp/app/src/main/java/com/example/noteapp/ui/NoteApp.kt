package com.example.noteapp.ui

import android.app.Application
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.home.HomeRoute
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.util.InjectorUtils
import com.example.noteapp.view_model.HomeViewModel

@Composable
fun NoteApp(
    application: Application
) {
    val homeViewModel: HomeViewModel = viewModel(
        factory = InjectorUtils.providerNoteViewModelFactory(application)
    )

    val navController = rememberNavController()

    NoteAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {

            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    HomeRoute(
                        homeViewModel = homeViewModel,
                    )
                }
            }

        }
    }
}