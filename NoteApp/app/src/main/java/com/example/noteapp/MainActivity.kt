package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.ui.detail.DetailScreen
import com.example.noteapp.ui.home.HomeScreen
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.ui.write.WriteScreen
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.util.InjectorUtils
import com.example.noteapp.view_model.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MainViewModel = viewModel(
                factory = InjectorUtils.providerNoteViewModelFactory(application)
            )

            val navController = rememberNavController()

            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onClickFloatingBtn = {
                                    navController.navigate("write")
                                },
                                onClickItem = { noteId ->
                                    DEVLogger.d("onClickItem $noteId")

                                    navController.navigate("detail?noteId=$noteId")
                                }
                            )
                        }
                        composable(route = "write") {
                            WriteScreen()
                        }
                        composable(route = "detail?noteId={noteId}",
                        arguments = listOf(
                            navArgument("noteId") {
                                type = NavType.IntType
                            }
                        )) { backStackEntry ->
                            var noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                            if(noteId == 0) {
                                navController.navigateUp()
                            } else {
                                DetailScreen(viewModel = viewModel, noteId = noteId)
                            }
                        }
                    }

                }
            }
        }
    }
}