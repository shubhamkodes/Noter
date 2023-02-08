package com.shubhamkodes.noter.android.note_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate("note_detail/-1L")
        }, backgroundColor = Color.Black) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
        }
    }) {
        Column(modifier = Modifier.padding(it)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HideableSearchTextField(
                    text = state.searchText,
                    isSearchActive = state.isSearchActive,
                    onTextChange = viewModel::onSearchTextChange,
                    onSearchClick = viewModel::onToggleSearch,
                    onCloseClick = viewModel::onToggleSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )

                this@Column.AnimatedVisibility(visible = !state.isSearchActive) {
                    Text(text = "All Notes", fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.notes, key = { note -> note.id }) { note ->
                    NoteItem(
                        note = note,
                        backgroundColor = Color(note.colorHex),
                        onNoteClick = {
                                      navController.navigate("note_detail/${note.id}")
                        },
                        onDeleteClick = { viewModel.deleteNoteById(note.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItemPlacement()
                    )
                }

                item {

                    AnimatedVisibility(
                        visible = state.isSearchActive && state.searchText.length > 3 && state.notes.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        EmptyListInfo(
                            drawableId = com.shubhamkodes.noter.android.R.drawable.ic_empty_search_list,
                            message = "Not found!!",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    AnimatedVisibility(
                        visible = state.notes.isEmpty() && !state.isSearchActive,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        EmptyListInfo(
                            drawableId = com.shubhamkodes.noter.android.R.drawable.ic_empty_list,
                            message = "No Note Yet, Create new note now...",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }


    }

}

@Composable
private fun EmptyListInfo(drawableId: Int, message: String, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message, fontSize = 16.sp, fontWeight = FontWeight.Normal)
    }
}