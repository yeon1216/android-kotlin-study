package com.example.noteapp.ui.home

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
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
        onInteractWithNoteList = { homeViewModel.interactWithNoteList() },
        onErrorDismiss = { errorId -> homeViewModel.errorShown(errorId) },
        onSelectedImg = { selectedImgUri -> homeViewModel.selectImg(selectedImgUri) }
    )

}

@Composable
fun HomeRoute(
    homeUiState: HomeUiState,
    onClickWriteBtn: (writeNote: Note) -> Unit,
    onInteractWithNoteWrite: () -> Unit,
    onInteractWithNoteDetail: (noteId: Int) -> Unit,
    onInteractWithNoteList: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onSelectedImg: (selectedImgUri: Uri) -> Unit
) {

    val scaffoldState = rememberScaffoldState()

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
                onClickWriteBtn = onClickWriteBtn,
                onSelectedImg = onSelectedImg,
                selectedImgUri = homeUiState.selectedImgUri
            )
            BackHandler {
                onInteractWithNoteList()
            }
        }
    }

    if(homeUiState.errorMessage.id != 0L) {
        val errorMessage = remember(homeUiState) { homeUiState.errorMessage }
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
