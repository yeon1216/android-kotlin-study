package com.example.noteapp.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.noteapp.ui.detail.DetailScreen
import com.example.noteapp.ui.write.WriteScreen
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.view_model.HomeUiState
import com.example.noteapp.view_model.HomeViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel
) {

    val homeUiState: HomeUiState by homeViewModel.uiState.collectAsState()
    HomeRoute(
        homeUiState = homeUiState,
        onInteractWithNoteWrite = {
            DEVLogger.d("onInteractWithNoteWrite()")
            homeViewModel.interactWithNoteWrite()
        },
        onInteractWithNoteDetail = { noteId -> homeViewModel.interactWithNoteDetail(noteId) },
        onInteractWithNoteList = { homeViewModel.interactWithNoteList() }
    )

}

@Composable
fun HomeRoute(
    homeUiState: HomeUiState,
    onInteractWithNoteWrite: () -> Unit,
    onInteractWithNoteDetail: (noteId: Int) -> Unit,
    onInteractWithNoteList: () -> Unit,
) {

    when(getHomeScreenType(homeUiState)) {
        HomeScreenType.NoteList -> {
            DEVLogger.d("HomeScreenType.NoteList")
            HomeScreen(
                uiState = homeUiState,
                onClickFloatingBtn = onInteractWithNoteWrite,
                onClickItem = onInteractWithNoteDetail
            )
        }
        HomeScreenType.NoteDetail -> {
            DEVLogger.d("HomeScreenType.NoteDetail")
            check(homeUiState is HomeUiState.HasNotes)
            DetailScreen(homeUiState.selectedNote)
            BackHandler {
                onInteractWithNoteList()
            }
        }
        HomeScreenType.NoteWrite -> {
            DEVLogger.d("HomeScreenType.NoteWrite")
            WriteScreen()
            BackHandler {
                onInteractWithNoteList()
            }
        }
    }

}


private enum class HomeScreenType {
    NoteDetail,
    NoteList,
    NoteWrite
}

@Composable
private fun getHomeScreenType(
    uiState: HomeUiState
): HomeScreenType = when (uiState) {
    is HomeUiState.HasNotes -> {
        if (uiState.isNoteOpen) {
            HomeScreenType.NoteDetail
        } else if(uiState.isWriteOpen){
            HomeScreenType.NoteWrite
        } else {
            HomeScreenType.NoteList
        }
    }
    is HomeUiState.NoNotes -> {
        if (uiState.isWriteOpen) {
            HomeScreenType.NoteWrite
        } else {
            HomeScreenType.NoteList
        }

    }

}
