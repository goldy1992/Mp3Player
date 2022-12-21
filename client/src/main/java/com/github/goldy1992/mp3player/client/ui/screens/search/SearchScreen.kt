package com.github.goldy1992.mp3player.client.ui.screens.search

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
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.buildOnSelectedMap
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderListItem
import com.github.goldy1992.mp3player.client.ui.lists.onFolderSelected
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongListItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import org.apache.commons.lang3.StringUtils
import java.util.*

private const val logTag = "SearchScreen"

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    navController: NavController,
    windowSize: WindowSize,
    viewModel : SearchScreenViewModel = viewModel()) {

    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val onSelectedMap = {
        buildOnSelectedMap(
            onFolderSelected = onFolderSelected(navController),
            onSongsSelected = {
                itemIndex : Int, mediaItemList : List<MediaItem> ->
                    viewModel.playFromList(itemIndex, mediaItemList)
            },
            onSongSelected = {
                song -> viewModel.play(song)
            }
        )
    }

    val navDrawerContent : @Composable () -> Unit = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.SEARCH
        )
    }

    val onNavUpPressed : () -> Unit = {  navController.popBackStack() }

    val topBar : @Composable () -> Unit = {
        SearchBar(
            currentSearchQuery = { searchQuery },
            onNavUpPressed = onNavUpPressed,
            onSearchQueryUpdated = {
                Log.i(logTag, "setting searchQuery with value: ${it}")
                viewModel.setSearchQuery(it) },
            onSearchQueryCleared = { viewModel.setSearchQuery("") },
        )
    }

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(isPlayingProvider = { isPlaying },
            onClickSkipNext = { viewModel.skipToNext() },
            onClickSkipPrevious = { viewModel.skipToPrevious() },
            onClickPause = { viewModel.pause() },
            onClickPlay = { viewModel.play() },
            onClickBar = {navController.navigate(Screen.NOW_PLAYING.name)}
           )
    }


    val isLargeScreen = windowSize == WindowSize.Expanded
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

@OptIn(ExperimentalMaterial3Api::class)
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


@OptIn(ExperimentalMaterial3Api::class)
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


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
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
                    Icon(Icons.Filled.ArrowBack, "Back")
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

@OptIn(ExperimentalCoilApi::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun SearchResultsContent(
    modifier : Modifier = Modifier,
    searchResultsProvider : () -> List<MediaItem> = { emptyList() },
    onSelectedMapProvider : () -> EnumMap<MediaItemType, Any> = { EnumMap(MediaItemType::class.java) },
    keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester : FocusRequester = remember { FocusRequester() }) {


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

    if (isNotEmpty(searchResults)) {
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = searchResultsColumn
            },
        state = lazyListState
        ) {
            items(count = searchResults.size) { itemIndex ->
                run {
                    val mediaItem = searchResults[itemIndex]
                    when (MediaItemUtils.getMediaItemType(mediaItem)) {
                        MediaItemType.SONG -> {
                            SongListItem(song = mediaItem, onClick = {
                                val onSongSelected = onSelectedMap[MediaItemType.SONG] as (MediaItem) -> Unit
                                onSongSelected(mediaItem)
                            })
                        }
                        MediaItemType.FOLDER -> {
                            FolderListItem(folder = mediaItem, onClick = {
                                val onFolderSelected = onSelectedMap[MediaItemType.FOLDER] as (MediaItem) -> Unit
                                onFolderSelected(mediaItem)
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