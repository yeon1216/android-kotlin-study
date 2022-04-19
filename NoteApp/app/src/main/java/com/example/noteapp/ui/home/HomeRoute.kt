package com.example.noteapp.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.noteapp.domain.model.Note
import com.example.noteapp.view_model.HomeUiState
import com.example.noteapp.view_model.HomeViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel
) {

    val homeUiState: HomeUiState by homeViewModel.uiState.collectAsState()
    HomeRoute(
        homeUiState = homeUiState,
        onClickWriteBtn = {
            homeViewModel.insertNote(it)
            homeViewModel.interactWithNoteList()
            homeViewModel.refreshNotes()
        },
        onInteractWithNoteWrite = {
            homeViewModel.interactWithNoteWrite()
        },
        onInteractWithNoteDetail = { noteId -> homeViewModel.interactWithNoteDetail(noteId) },
        onInteractWithNoteList = { homeViewModel.interactWithNoteList() }
    )

}

@Composable
fun HomeRoute(
    homeUiState: HomeUiState,
    onClickWriteBtn: (writeNote: Note) -> Unit,
    onInteractWithNoteWrite: () -> Unit,
    onInteractWithNoteDetail: (noteId: Int) -> Unit,
    onInteractWithNoteList: () -> Unit,
) {

    when(getHomeScreenType(homeUiState)) {
        HomeScreenType.NoteList -> {
            HomeScreen(
                uiState = homeUiState,
                onClickFloatingBtn = onInteractWithNoteWrite,
                onClickItem = onInteractWithNoteDetail
            )
        }
        HomeScreenType.NoteDetail -> {
            check(homeUiState is HomeUiState.HasNotes)
            DetailScreen(
                homeUiState.selectedNote,
                onClickArrowBack = onInteractWithNoteList
            )
            BackHandler {
                onInteractWithNoteList()
            }
        }
        HomeScreenType.NoteWrite -> {
            WriteScreen(
                onClickWriteBtn = onClickWriteBtn
            )
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
