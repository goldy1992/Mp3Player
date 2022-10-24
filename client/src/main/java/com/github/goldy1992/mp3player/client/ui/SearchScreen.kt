package com.github.goldy1992.mp3player.client.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderListItem
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongListItem
import com.github.goldy1992.mp3player.client.viewmodels.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import org.apache.commons.lang3.StringUtils

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    navController: NavController,
    windowSize: WindowSize,
    viewModel : SearchScreenViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()) {

    val isLargeScreen = windowSize == WindowSize.Expanded
    if (isLargeScreen) {
          LargeSearchResults(
            searchResultsState = viewModel.searchResults,
            navController = navController,
            mediaBrowser = viewModel.mediaBrowserAdapter,
            mediaController = viewModel.mediaControllerAdapter,
            isPlayingState = viewModel.isPlaying,
            scope = scope

        )
    } else {
        SmallSearchResults(
            searchResultsState = viewModel.searchResults,
            navController = navController,
            mediaBrowser = viewModel.mediaBrowserAdapter,
            mediaController = viewModel.mediaControllerAdapter,
            isPlayingState = viewModel.isPlaying,
            scope = scope
        )

    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun SmallSearchResults(
    searchResultsState : StateFlow<List<MediaItem>>,
    navController: NavController,
    mediaBrowser: MediaBrowserAdapter,
    mediaController : MediaControllerAdapter,
    isPlayingState: StateFlow<Boolean>,
    scope : CoroutineScope = rememberCoroutineScope()) {
    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController
            ) },
        drawerState = drawerState) {

        Scaffold(
            topBar = {
                SearchBar(
                    navController = navController,
                    mediaBrowser = mediaBrowser,
                    scope = scope
                )
            },
            bottomBar = {
                PlayToolbar(mediaController = mediaController,
                            isPlayingState = isPlayingState,
                            scope = scope) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }
            },
            content = {
                SearchResults(
                    searchResultsState = searchResultsState,
                    mediaController = mediaController,
                    navController = navController,
                    modifier = Modifier.padding(it)
                )
            }

        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun LargeSearchResults(
    searchResultsState : StateFlow<List<MediaItem>>,
    navController: NavController,
    mediaBrowser: MediaBrowserAdapter,
    mediaController : MediaControllerAdapter,
    isPlayingState: StateFlow<Boolean>,
    scope : CoroutineScope = rememberCoroutineScope()) {
    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                currentScreen = Screen.SEARCH
            ) },
    ) {
        Scaffold(
            topBar = {
                SearchBar(
                    navController = navController,
                    mediaBrowser = mediaBrowser,
                    scope = scope
                )
            },
            bottomBar = {
                PlayToolbar(mediaController = mediaController,
                            isPlayingState = isPlayingState,
                            scope = scope) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }
            },
            content = {
                Surface(
                    Modifier
                        .background(Color.Yellow)
                        .width(500.dp)
                        .fillMaxHeight()
                ) {
                    SearchResults(
                        searchResultsState = searchResultsState,
                        mediaController = mediaController,
                        navController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun SearchBar(navController: NavController,
              mediaBrowser: MediaBrowserAdapter,
              keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
              focusRequester : FocusRequester = remember { FocusRequester() },
                scope: CoroutineScope = rememberCoroutineScope()) {
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
                .background(MaterialTheme.colorScheme.background)
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
                scope.launch {
                    searchQuery.value = it
                    mediaBrowser.search(searchQuery.value.text, Bundle())
                }
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search_hint))
            },
            leadingIcon = {
                IconButton(onClick = {
                    scope.launch {
                     //   mediaBrowser.clearSearchResults()
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            trailingIcon = {
                if (StringUtils.isNotEmpty(searchQuery.value.text)) {
                    IconButton(onClick = { searchQuery.value = TextFieldValue()
                  //      mediaBrowser.clearSearchResults()
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

@OptIn(ExperimentalCoilApi::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun SearchResults(searchResultsState : StateFlow<List<MediaItem>>,
                  mediaController: MediaControllerAdapter,
                  navController: NavController,
                  modifier : Modifier = Modifier,
                  keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
                  focusRequester : FocusRequester = remember { FocusRequester() }) {
    val searchResultsColumn = stringResource(id = R.string.search_results_column)

    val searchResults by searchResultsState.collectAsState()
    val lazyLisState = rememberLazyListState()
    
    LaunchedEffect(lazyLisState.isScrollInProgress) {
        Log.i("launchedEffect", "called launch effect")
        if (lazyLisState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }


    if (isNotEmpty(searchResults)) {
        LazyColumn(modifier = modifier
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
                                mediaController.playFromMediaId(mediaItem)
                            })
                        }
                        MediaItemType.FOLDER -> {
                            FolderListItem(folder = mediaItem, onClick = {
                                val folderLibraryId = MediaItemUtils.getLibraryId(it)
                                val encodedFolderLibraryId = Uri.encode(folderLibraryId)
                                val directoryPath = MediaItemUtils.getDirectoryPath(it)
                                val encodedFolderPath = Uri.encode(directoryPath)
                                val folderName = MediaItemUtils.getDirectoryName(it)
                                navController.navigate(
                                    Screen.FOLDER.name
                                            + "/" + encodedFolderLibraryId
                                            + "/" + folderName
                                            + "/" + encodedFolderPath)
                            })
                        }
                        MediaItemType.SONGS, MediaItemType.FOLDERS -> {

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        top = 30.dp,
                                        bottom = 10.dp
                                    )
                            ) {
                                Text(
                                    text = MediaItemUtils.getTitle(mediaItem),
                                    style = MaterialTheme.typography.titleMedium,
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
        Column(Modifier
            .fillMaxSize()
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