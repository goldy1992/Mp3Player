package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import org.apache.commons.lang3.StringUtils

@Composable
fun SearchScreen(
    navController: NavController,
    mediaBrowser :  MediaBrowserAdapter,
    mediaController : MediaControllerAdapter) {

    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
        SearchBar(
            navController = navController,
            mediaBrowser = mediaBrowser
        )},
        bottomBar = {
        PlayToolbar(mediaController = mediaController) {
            navController.navigate(NOW_PLAYING_SCREEN)
        }
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = BOTTOM_BAR_SIZE)) {
                SearchResults(mediaBrowser = mediaBrowser)
            }
        }
    )
}




@Composable
fun SearchBar(navController: NavController,
              mediaBrowser: MediaBrowserAdapter) {
    val searchQuery = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .focusModifier()
            .fillMaxWidth()) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                mediaBrowser.search(searchQuery.value.text, null)},
            placeholder = {
                Text(text = "Search Media")
            },
            leadingIcon = {
                IconButton(onClick = {
                    scope.launch {
                        navController.popBackStack()
                    }
                }){
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            trailingIcon = {
                if (StringUtils.isNotEmpty(searchQuery.value.text)) {
                    IconButton(onClick = { searchQuery.value = TextFieldValue()  }) {
                        Icon(Icons.Outlined.Close, "Cancel")
                        mediaBrowser.clearSearchResults()
                    }
                }
            })

    }
}

@Composable
fun SearchResults(mediaBrowser: MediaBrowserAdapter) {

    val searchResults by mediaBrowser.searchResults().observeAsState(emptyList())

    if (isNotEmpty(searchResults)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = searchResults.size) { itemIndex ->
                val mediaItem = searchResults[itemIndex]
                when(MediaItemUtils.getMediaItemType(mediaItem)) {
                    MediaItemType.SONG -> {
                        SongListItem(song = mediaItem, onClick = {})
                    }
                    MediaItemType.FOLDER -> {
                        FolderListItem(folder = mediaItem, onClick = {} )
                    }
                    MediaItemType.ROOT -> {
                        Column(Modifier.fillMaxWidth()) {
                            Text(text = MediaItemUtils.getTitle(mediaItem))
                            // TODO: Add underscore line and padding.
                        }
                    }

                    else -> {
                        Log.i("SearchScreen", "Item found with no MediaItemType")
                    }
                }
            }
        }
    }
}