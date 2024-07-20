package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createAlbum
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createFolder
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createMediaItem
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.CustomCommandEvent
import com.github.goldy1992.mp3player.client.models.PlaybackParametersEvent
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.SearchResult
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getLibraryParams
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils.getRepeatMode
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.apache.commons.collections4.CollectionUtils
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

    override fun currentSong(): Flow<Song> {
        return mediaDataSource.currentMediaItem()
        .map {
            createSong(it)
        }
    }

    override fun currentPlaylistId(): Flow<String> {
        return mediaDataSource.currentPlaylistMetadata().map {
            val extras = it.extras
            val playlistId = extras?.getString(Constants.PLAYLIST_ID) ?: Constants.UNKNOWN
            Log.d(logTag(), "mediaRepository.currentPlaylistMetadata() collect: new playlist metadata retrieved: $playlistId")
            playlistId
        }
    }

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
                itemCount = it.itemCount,
                extras = LibraryParamsParser.parse(it.params)
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

    override fun playbackParameters(): Flow<PlaybackParametersEvent> {
        return mediaDataSource.playbackParameters().map {
            PlaybackParametersEvent(speed = it.speed,
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

    override fun queue(): Flow<Queue> {
        return mediaDataSource.queue().map {
            Queue(
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

    override suspend fun <T : MediaEntity> getChildren(
        parent: T,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): T {
        val mediaItems = mediaDataSource.getChildren(parent.id, page, pageSize, getLibraryParams(params))
        return MediaEntityParser.parse(parent, mediaItems)
    }

    override suspend fun getPlaylist(
        playlistId: String,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): Playlist {
        val songs = mediaDataSource.getChildren(playlistId, page, pageSize, MediaLibraryService.LibraryParams.Builder().setExtras(params).build())
        //val currentFolderValue = folder.value
        return if (CollectionUtils.isEmpty(songs)) {
            Playlist(
                id = playlistId,
                state = State.NO_RESULTS,
           )
        } else {
            Playlist(
                id = playlistId,
                state = State.LOADED,
                songs = songs.map { createSong(it) }.toList(),
            )
        }
    }


    override suspend fun getLibraryRoot(): Root {
        val rootMediaItem = mediaDataSource.getLibraryRoot()
        return Root(
            id = rootMediaItem.mediaId,
            state = State.NOT_LOADED
        )
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int) : SearchResults {
        val mediaItems = mediaDataSource.getSearchResults(query, page, pageSize)
        val resultsMap = mutableListOf<SearchResult>()

        mediaItems.forEach {
            val result : SearchResult =
                when (MediaItemUtils.getMediaItemType(it)) {
                    MediaItemType.SONG -> SearchResult(query, MediaItemType.SONG, createSong(it))
                    MediaItemType.FOLDER -> SearchResult(query, MediaItemType.FOLDER, createFolder(it))
                    MediaItemType.ALBUM -> SearchResult(query, MediaItemType.ALBUM, createAlbum(it))
                    else -> SearchResult(query, MediaItemUtils.getMediaItemType(it))
                }
            resultsMap.add(result)
        }

        return SearchResults(
            state = State.LOADED,
            resultsMap = resultsMap
        )
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

    override suspend fun playAlbum(album: Album, startIndex: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun playPlaylist(playlist: Playlist, startIndex: Int) {

        val mediaItems = playlist.songs.map { createMediaItem(it) }
        mediaDataSource.playFromPlaylist(mediaItems, startIndex, playlist.id)
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        mediaDataSource.playFromUri(uri, extras)
    }

    override suspend fun prepareFromId(mediaId: String) {
        mediaDataSource.prepareFromMediaId(mediaId)
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