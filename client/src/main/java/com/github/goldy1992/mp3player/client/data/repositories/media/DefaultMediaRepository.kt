package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.data.CustomCommandEvent
import com.github.goldy1992.mp3player.client.data.MediaEntity
import com.github.goldy1992.mp3player.client.data.PlaybackParameters
import com.github.goldy1992.mp3player.client.data.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.client.data.QueueChangedEvent
import com.github.goldy1992.mp3player.client.data.RepeatMode
import com.github.goldy1992.mp3player.client.data.Root
import com.github.goldy1992.mp3player.client.data.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createMediaItem
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.mapMediaItemsToMediaEntities
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils.getRepeatMode
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils.uiToPlayerMap

import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@UnstableApi
@ActivityRetainedScoped
class DefaultMediaRepository
    @Inject
    constructor(
        private val mediaDataSource : MediaDataSource
    ) : MediaRepository, LogTagger {

    override fun audioData(): Flow<AudioSample> {
        return mediaDataSource.audioData()
    }

    @UnstableApi
    override fun currentSong(): Flow<Song> {
        return mediaDataSource.currentMediaItem()
            .filter {
                val metadata = it.mediaMetadata
                !(metadata.isBrowsable ?: false) && (metadata.isPlayable ?: false)
            }.map {
            createSong(it)
        }
    }

//    override fun currentPlaylistId(): Flow<String> {
//        return mediaDataSource.currentPlaylistMetadata().map {
//            it.e
//        }
//    }

    override fun currentSearchQuery(): Flow<String> {
        return mediaDataSource.currentSearchQuery()
    }

    override fun isPlaying(): Flow<Boolean> {
        return mediaDataSource.isPlaying()
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return mediaDataSource.isShuffleModeEnabled()
    }

    override fun onChildrenChanged(): Flow<ChildrenChangedEvent> {
        return mediaDataSource.onChildrenChanged().map {
            ChildrenChangedEvent(
                parentId = it.parentId,
                itemCount = it.itemCount
            )
        }
    }

    override fun onCustomCommand() : Flow<CustomCommandEvent> {
        return mediaDataSource.onCustomCommand().map {
            CustomCommandEvent(
                id = it.command.customAction,
                args = it.args
            )
        }
    }

    override fun  onSearchResultsChanged() : Flow<SearchResultsChangedEvent> {
        return mediaDataSource.onSearchResultsChanged().map {
            SearchResultsChangedEvent(
                query = it.query,
                itemCount = it.itemCount,
                params = it.params?.extras
            )
        }
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        return mediaDataSource.playbackParameters().map {
            PlaybackParameters(speed = it.speed,
            pitch = it.pitch)
        }
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return mediaDataSource.playbackPosition().map {
            PlaybackPositionEvent(isPlaying = it.isPlaying,
            currentPosition = it.currentPosition,
            systemTime = it.systemTime
            )
        }
    }

    override fun playbackSpeed(): Flow<Float> {
        return mediaDataSource.playbackSpeed()
    }

    override fun queue(): Flow<QueueChangedEvent> {
        return mediaDataSource.queue().map {
            QueueChangedEvent(
                items = it.items.map { mediaItem -> createSong(mediaItem) }.toList(),
                currentIndex = it.currentIndex
            )
        }
    }

    override fun repeatMode(): Flow<RepeatMode> {
        return mediaDataSource.repeatMode().map {
            RepeatModeUtils.getRepeatMode(it)
        }
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        mediaDataSource.changePlaybackSpeed(speed)
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): List<MediaEntity> {
        val paramsToSend = MediaLibraryService.LibraryParams.Builder().setExtras(params).build()
        val mediaItems =  mediaDataSource.getChildren(parentId, page, pageSize, paramsToSend)
        return  mapMediaItemsToMediaEntities(mediaItems)
    }


    override suspend fun getLibraryRoot(): Root {
        val rootMediaItem = mediaDataSource.getLibraryRoot()
        return Root(rootMediaItem.mediaId)
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int) : List<MediaItem> {
        return mediaDataSource.getSearchResults(query, page, pageSize)
    }

    override suspend fun pause() {
        mediaDataSource.pause()
    }

    override suspend fun play() {
        mediaDataSource.play()
    }

    override suspend fun play(song: Song) {
        mediaDataSource.play(createMediaItem(song))
    }

//    override suspend fun playFromPlaylist(items: List<MediaItem>, itemIndex: Int, playlistMetadata: MediaMetadata) {
//        mediaDataSource.playFromPlaylist(items, itemIndex, playlistMetadata)
//    }

    override suspend fun playPlaylist(playlist: Playlist, startIndex: Int) {

        val mediaItems = playlist.songs.map { createMediaItem(it) }
        mediaDataSource.playFromPlaylist(mediaItems, startIndex, MediaMetadata.Builder().)
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        mediaDataSource.playFromUri(uri, extras)
    }

    override suspend fun prepareFromId(mediaItem: MediaItem) {
        mediaDataSource.prepareFromMediaId(mediaItem)
    }

    override suspend fun search(query: String, extras: Bundle) {
        mediaDataSource.search(query, extras)
    }

    override suspend fun seekTo(position: Long) {
        mediaDataSource.seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: RepeatMode) {
        mediaDataSource.setRepeatMode(getRepeatMode(repeatMode))
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        mediaDataSource.setShuffleMode(shuffleModeEnabled)
    }

    override suspend fun skipToNext() {
        mediaDataSource.skipToNext()
    }

    override suspend fun skipToPrevious() {
        mediaDataSource.skipToPrevious()
    }

    override suspend fun stop() {
        mediaDataSource.stop()
    }

    override suspend fun subscribe(id : String) {
        mediaDataSource.subscribe(id)
    }

    override fun logTag(): String {
        return "DefaultMediaBrowserRepo"
    }
}