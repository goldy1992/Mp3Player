package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import org.apache.commons.lang3.StringUtils

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    navController: NavController,
    mediaBrowser :  MediaBrowserAdapter,
    mediaController : MediaControllerAdapter,
    mediaRepository: MediaRepository) {

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
                SearchResults(mediaBrowser = mediaBrowser, mediaController = mediaController, mediaRepository = mediaRepository, navController = navController)
            }
        }
    )
}




@ExperimentalComposeUiApi
@Composable
fun SearchBar(navController: NavController,
              mediaBrowser: MediaBrowserAdapter) {
    val searchQuery = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Column(
        Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged {
                    Log.i("SearchScreen", "Focus changed: $it")
                    if (it.isFocused) {
                        keyboardController?.show()
                    }},
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                mediaBrowser.search(searchQuery.value.text, null)
            },
            placeholder = {
                Text(text = "Search Media")
            },
            leadingIcon = {
                IconButton(onClick = {
                    scope.launch {
                        mediaBrowser.clearSearchResults()
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            trailingIcon = {
                if (StringUtils.isNotEmpty(searchQuery.value.text)) {
                    IconButton(onClick = { searchQuery.value = TextFieldValue()
                        mediaBrowser.clearSearchResults()
                    }) {
                        Icon(Icons.Outlined.Close, "Cancel")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            )
        )

    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }

}

@Composable
fun SearchResults(mediaBrowser: MediaBrowserAdapter,
                  mediaController: MediaControllerAdapter,
                  mediaRepository: MediaRepository,
                navController: NavController) {

    val searchResults by mediaBrowser.searchResults().observeAsState(emptyList())

    if (isNotEmpty(searchResults)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = searchResults.size) { itemIndex ->
                val mediaItem = searchResults[itemIndex]
                when(MediaItemUtils.getMediaItemType(mediaItem)) {
                    MediaItemType.SONG -> {
                        SongListItem(song = mediaItem, onClick = {
                            val libraryId = MediaItemUtils.getLibraryId(mediaItem)
                            Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                            mediaController.playFromMediaId(libraryId, null)
                        })
                    }
                    MediaItemType.FOLDER -> {
                        FolderListItem(folder = mediaItem, onClick = {
                            mediaRepository.currentFolder = mediaItem
                            navController.navigate(FOLDER_SCREEN)

                        } )
                    }
                    MediaItemType.ROOT -> {
                        Column(Modifier
                            .fillMaxWidth()
                            .padding(
                                start = DEFAULT_PADDING,
                                top = 7.dp,
                                bottom = 2.dp)) {
                        Text(text = MediaItemUtils.getTitle(mediaItem),
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(2.dp)
                            )
                            Divider(
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp))
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