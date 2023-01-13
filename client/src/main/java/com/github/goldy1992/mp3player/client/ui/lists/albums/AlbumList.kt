package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import org.apache.commons.collections4.CollectionUtils

@Composable
fun AlbumsList(albums : List<MediaItem>) {
    when {
        CollectionUtils.isEmpty(albums) -> LoadingIndicator()
        MediaItemUtils.noResultsFound(albums) -> NoResultsFound(mediaItemType = MediaItemType.ALBUMS)
    }
}