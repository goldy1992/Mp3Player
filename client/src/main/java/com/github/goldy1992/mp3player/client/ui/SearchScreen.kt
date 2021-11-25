package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import org.apache.commons.lang3.StringUtils

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    navController: NavController,
    mediaBrowser :  MediaBrowserAdapter,
    mediaController : MediaControllerAdapter,
    mediaRepository: MediaRepository) {

    val scaffoldState = rememberScaffoldState()
    val keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
        SearchBar(
            navController = navController,
            mediaBrowser = mediaBrowser,
            keyboardController = keyboardController
        )},
        bottomBar = {
            PlayToolbar(mediaController = mediaController) {
                navController.navigate(Screen.NOW_PLAYING.name)
            }
        },
        content = {
            SearchResults(mediaBrowser = mediaBrowser,
                mediaController = mediaController,
                mediaRepository = mediaRepository,
                navController = navController)
        }

    )
}




@ExperimentalComposeUiApi
@Composable
fun SearchBar(navController: NavController,
              mediaBrowser: MediaBrowserAdapter,
              keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
              focusRequester : FocusRequester = remember { FocusRequester() }) {
    val searchQuery = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()


    Column(
        Modifier.fillMaxWidth()
    ) {
        val searchTextFieldName = stringResource(id = R.string.search_text_field)
        val clearSearchDescr = stringResource(id = R.string.clear_search)
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                }
                .semantics {
                    contentDescription = searchTextFieldName
                },
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                mediaBrowser.search(searchQuery.value.text, null)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search_hint))
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
                        Icon(Icons.Outlined.Close, clearSearchDescr)
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

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun SearchResults(mediaBrowser: MediaBrowserAdapter,
                  mediaController: MediaControllerAdapter,
                  mediaRepository: MediaRepository,
                  navController: NavController,
                  keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
                  focusRequester : FocusRequester = remember { FocusRequester() }) {
    val searchResultsColumn = stringResource(id = R.string.search_results_column)

    val searchResults by mediaBrowser.searchResults().observeAsState(emptyList())
    //val scrollState = rememberScrollState()
    val lazyLisState = rememberLazyListState()
    
    LaunchedEffect(lazyLisState.isScrollInProgress) {
        Log.i("launchedEffect", "called launch effect")
        if (lazyLisState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }


    if (isNotEmpty(searchResults)) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = searchResultsColumn
            },
        state = lazyLisState
        ) {
            items(count = searchResults.size) { itemIndex ->
                run {
                    val mediaItem = searchResults[itemIndex]
                    when (MediaItemUtils.getMediaItemType(mediaItem)) {
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
                                navController.navigate(Screen.FOLDER.name)

                            })
                        }
                        MediaItemType.ROOT -> {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = DEFAULT_PADDING,
                                        top = 7.dp,
                                        bottom = 2.dp
                                    )
                            ) {
                                Text(
                                    text = MediaItemUtils.getTitle(mediaItem),
                                    style = MaterialTheme.typography.subtitle2,
                                    modifier = Modifier.padding(2.dp)
                                )
                                Divider(
                                    color = Color.Black,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 2.dp)
                                )
                            }
                        }

                        else -> {
                            Log.i("SearchScreen", "Item found with no MediaItemType")
                        }
                    }
                }
            }
        }
    } else {
        Column(Modifier.fillMaxSize()
            .semantics {
          contentDescription = searchResultsColumn
            }
            .clickable {
                keyboardController?.hide()
            }
        ){

        }
    }
}