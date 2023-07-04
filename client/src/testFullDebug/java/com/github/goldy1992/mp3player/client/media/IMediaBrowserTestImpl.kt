package com.github.goldy1992.mp3player.client.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.QueueState
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IMediaBrowserTestImpl() : IMediaBrowser {
    override fun init(sessionToken: SessionToken, scope: CoroutineScope) {

    }

    override fun audioData(): Flow<AudioSample> {
        return flow { emit(AudioSample.NONE) }
    }

    override fun currentMediaItem(): Flow<MediaItem> {
        return flow { emit(MediaItem.EMPTY)}
    }

    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        return flow { emit(MediaMetadata.EMPTY)}
    }

    override fun isPlaying(): Flow<Boolean> {
        return flow { emit(false)}
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return flow { emit(false)}
    }

    override fun metadata(): Flow<MediaMetadata> {
        return flow { emit(MediaMetadata.EMPTY) }
    }

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return flow { emit(OnChildrenChangedEventHolder.DEFAULT)}
    }

    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return flow { emit(SessionCommandEventHolder.DEFAULT)}
    }

    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return flow { emit(OnSearchResultsChangedEventHolder.DEFAULT)}
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        return flow { emit(PlaybackParameters.DEFAULT)}
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return flow { emit(PlaybackPositionEvent.DEFAULT)}
    }

    override fun playbackSpeed(): Flow<Float> {
        return flow { emit(1f)}
    }

    override fun queue(): Flow<QueueState> {
        return flow { emit(QueueState.EMPTY)}
    }

    override fun repeatMode(): Flow<Int> {
        return flow { emit(Player.REPEAT_MODE_OFF)}
    }

    override suspend fun changePlaybackSpeed(speed: Float) {

    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        return 0L
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return emptyList()
    }

    override suspend fun getLibraryRoot(): MediaItem {
        return MediaItem.EMPTY
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        return emptyList()
    }

    override suspend fun pause() {

    }

    override suspend fun play() {

    }

    override suspend fun play(mediaItem: MediaItem) {

    }

    override suspend fun playFromPlaylist(
        items: List<MediaItem>,
        itemIndex: Int,
        playlistMetadata: MediaMetadata
    ) {

    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {

    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {

    }

    override suspend fun search(query: String, extras: Bundle) {

    }

    override suspend fun seekTo(position: Long) {

    }

    override suspend fun setRepeatMode(repeatMode: Int) {

    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {

    }

    override suspend fun skipToNext() {

    }

    override suspend fun skipToPrevious() {

    }

    override suspend fun stop() {

    }

    override suspend fun subscribe(id: String) {

    }

    override fun release() {

    }
}