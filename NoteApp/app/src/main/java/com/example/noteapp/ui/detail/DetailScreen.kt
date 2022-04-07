package com.example.noteapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.view_model.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    noteId: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp)
                        )
                        Text(
//                            text = stringResource(id = R.string.published_in, post.publication?.name ?: ""),
                            text = "Detail",
                            style = MaterialTheme.typography.subtitle2,
                            color = LocalContentColor.current,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1.5f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { DEVLogger.d("onClick back") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = stringResource(R.string.cd_navigate_up),
                            contentDescription = "up",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
//                elevation = if (!lazyListState.isScrolled) 0.dp else 4.dp,
                backgroundColor = MaterialTheme.colors.surface
            )
        },
//        bottomBar = bottomBarContent
    ) {

    }
}

fun LazyListScope.noteContentItems(
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    noteId: Int
) {
    coroutineScope.launch {
        viewModel.getNote(noteId = noteId)
    }
}