package com.github.goldy1992.mp3player.client.ui.screens.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.UiConstants.DEFAULT_DP_SIZE
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.albums.AlbumSearchResultItem
import com.github.goldy1992.mp3player.client.ui.lists.buildOnSelectedMap
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderListItem
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongListItem
import com.github.goldy1992.mp3player.client.utils.NavigationUtils
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import org.apache.commons.lang3.StringUtils
import java.util.*

private const val logTag = "SearchScreen"


@Composable
fun SharedTransitionScope.SearchScreen(
    navController: NavController = rememberNavController(),
    windowSize: WindowSizeClass = WindowSizeClass.calculateFromSize(DEFAULT_DP_SIZE),
    viewModel: SearchScreenViewModel = viewModel(),
    animatedContentScope: AnimatedVisibilityScope
) {
    Log.i(logTag,"composing search screen")
    val playbackState by viewModel.playbackState.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()


    Log.i(logTag,"state_collected")
    val onSelectedMap = {
        buildOnSelectedMap(
            onAlbumSelected = { album -> NavigationUtils.navigate(navController, album)  },
            onFolderSelected = { folder -> NavigationUtils.navigate(navController, folder) },
            onSongsSelected = {
                itemIndex : Int, mediaItemList : Playlist ->
                    viewModel.playPlaylist(mediaItemList, itemIndex)
            },
            onSongSelected = { song -> viewModel.play(song) }
        )
    }

    val navDrawerContent : @Composable () -> Unit = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.SEARCH
        )
    }

    val onNavUpPressed : () -> Unit = {  navController.popBackStack() }

    Log.i(logTag, "creating top bar")
    val topBar : @Composable () -> Unit = {
        SearchBar(
            currentSearchQuery = { searchQuery },
            onNavUpPressed = onNavUpPressed,
            onSearchQueryUpdated = {
                Log.i(logTag, "setting searchQuery with value: $it")
                viewModel.setSearchQuery(it) },
            onSearchQueryCleared = { viewModel.setSearchQuery("") },
        )
    }
    Log.i(logTag, "created top bar")

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(
            playbackState = playbackState,
            onClickBar = {navController.navigate(Screen.NOW_PLAYING.name)},
            animatedVisibilityScope = animatedContentScope
       )
    }

    val isLargeScreen = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
    if (isLargeScreen) {
        LargeSearchResults(
            topBar = topBar,
            navDrawerContent = navDrawerContent,
            bottomBar = bottomBar) {
            SearchResultsContent(
                modifier = Modifier.padding(it),
                searchResultsProvider = { searchResults },
                onSelectedMapProvider = onSelectedMap
            )
          }
    } else {
        SmallSearchResults(
          topBar = topBar,
            navDrawerContent = navDrawerContent,
            bottomBar = bottomBar) {
            SearchResultsContent(
                modifier = Modifier.padding(it),
                searchResultsProvider = { searchResults },
                onSelectedMapProvider = onSelectedMap
            )
        }

    }
}

@Composable
private fun SmallSearchResults(
    topBar : @Composable () -> Unit = {},
    navDrawerContent : @Composable () -> Unit = {},
    bottomBar : @Composable () -> Unit = {},
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content : @Composable (PaddingValues) -> Unit = {}
    ) {
    ModalNavigationDrawer(
        drawerContent = navDrawerContent,
        drawerState = drawerState) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }
}

@Composable
private fun LargeSearchResults(
    topBar : @Composable () -> Unit = {},
    navDrawerContent : @Composable () -> Unit = {},
    bottomBar : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit = {}) {
    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = navDrawerContent,
    ) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }
}


@Composable
fun SearchBar(currentSearchQuery : () -> String = { "No search query specified" },
              onNavUpPressed : () -> Unit = {},
              onSearchQueryUpdated : (String) -> Unit = {},
              onSearchQueryCleared : () -> Unit = {},
              keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
              focusRequester : FocusRequester = remember { FocusRequester() }) {


    val currentSearchQueryValue = currentSearchQuery()
    Log.i(logTag, "currentSearchQueryValue : $currentSearchQueryValue")
    var textFieldValueState by remember(currentSearchQueryValue) {
        mutableStateOf(
            TextFieldValue(
                text =  currentSearchQueryValue,
                selection = TextRange(currentSearchQueryValue.length)
            )
        )
    }
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
            value = textFieldValueState,
            onValueChange = {
                onSearchQueryUpdated(it.text)
                textFieldValueState = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search_hint))
            },
            leadingIcon = {
                IconButton(onClick = onNavUpPressed) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            trailingIcon = {
                if (StringUtils.isNotEmpty(currentSearchQueryValue)) {
                    IconButton(onClick = {
                        onSearchQueryCleared()
                        textFieldValueState = TextFieldValue()
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

@Composable
fun SearchResultsContent(
    modifier : Modifier = Modifier,
    searchResultsProvider : () -> SearchResults = { SearchResults.NO_RESULTS },
    onSelectedMapProvider : () -> EnumMap<MediaItemType, Any> = { EnumMap(MediaItemType::class.java) },
    keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester : FocusRequester = remember { FocusRequester() }) {

    Log.i(logTag, "composing search result content")
    val searchResultsColumn = stringResource(id = R.string.search_results_column)
    val onSelectedMap = onSelectedMapProvider()
    val searchResults = searchResultsProvider()
    val lazyListState = rememberLazyListState()
    
    LaunchedEffect(lazyListState.isScrollInProgress) {
        Log.i("launchedEffect", "called launch effect")
        if (lazyListState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }

    if (searchResults.hasResults()) {
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = searchResultsColumn
            },
        state = lazyListState
        ) {
            items(count = searchResults.resultsMap.size) { itemIndex ->
                run {
                    val searchResult = searchResults.getResult(itemIndex)
                    when (searchResult.type) {
                        MediaItemType.SONG -> {
                            val song = searchResult.value as Song
                            SongListItem(song = song, onClick = {
                                val onSongSelected = onSelectedMap[MediaItemType.SONG] as (Song) -> Unit
                                onSongSelected(song)
                            })
                        }
                        MediaItemType.FOLDER -> {
                            val folder = searchResult.value as Folder
                            FolderListItem(folder = folder, onClick = {
                                val onFolderSelected = onSelectedMap[MediaItemType.FOLDER] as (Folder) -> Unit
                                onFolderSelected(folder)
                            })
                        }
                        MediaItemType.ALBUM -> {
                            val album = searchResult.value as Album
                            AlbumSearchResultItem(album = album, onClick = {
                                val onAlbumSelected = onSelectedMap[MediaItemType.ALBUM] as (Album) -> Unit
                                onAlbumSelected(album)
                            })
                        }
                        MediaItemType.SONGS, MediaItemType.FOLDERS, MediaItemType.ALBUMS -> {
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
                                    text = searchResult.type.name,
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
    Log.i(logTag,"composed search screen content")
}