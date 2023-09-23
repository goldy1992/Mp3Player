package com.github.goldy1992.mp3player.client.media

import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.Events
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent

class PlayerTestImpl() : Player {
    private val listeners = mutableSetOf<Player.Listener>()

    private fun updateListeners(updateLambda : (Player.Listener) -> Unit) {
        for (listener in listeners) {
            updateLambda(listener)
        }
    }
    override fun getApplicationLooper(): Looper {
        TODO("Not yet implemented")
    }

    override fun addListener(listener: Player.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Player.Listener) {
        listeners.remove(listener)
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem, startPositionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMediaItem(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun addMediaItem(index: Int, mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun addMediaItems(mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun addMediaItems(index: Int, mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun moveMediaItem(currentIndex: Int, newIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun replaceMediaItem(index: Int, mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: MutableList<MediaItem>
    ) {
        TODO("Not yet implemented")
    }

    override fun removeMediaItem(index: Int) {
        TODO("Not yet implemented")
    }

    override fun removeMediaItems(fromIndex: Int, toIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun clearMediaItems() {
        TODO("Not yet implemented")
    }

    override fun isCommandAvailable(command: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun canAdvertiseSession(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAvailableCommands(): Player.Commands {
        TODO("Not yet implemented")
    }

    override fun prepare() {
        TODO("Not yet implemented")
    }

    override fun getPlaybackState(): Int {
        TODO("Not yet implemented")
    }

    override fun getPlaybackSuppressionReason(): Int {
        TODO("Not yet implemented")
    }

    var isPlayingTestValue = false
    fun setIsPlaying(isPlaying : Boolean) {
        this.isPlayingTestValue = isPlaying
        updateListeners { l -> l.onIsPlayingChanged(isPlaying) }
    }
    override fun isPlaying(): Boolean {
        return isPlayingTestValue
    }

    override fun getPlayerError(): PlaybackException? {
        TODO("Not yet implemented")
    }

    override fun play() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getPlayWhenReady(): Boolean {
        TODO("Not yet implemented")
    }

    var testRepeatMode = Player.REPEAT_MODE_OFF
    override fun setRepeatMode(repeatMode: Int) {
        this.testRepeatMode = repeatMode
        updateListeners { it.onRepeatModeChanged(testRepeatMode) }
    }

    override fun getRepeatMode(): Int {
        return testRepeatMode
    }

    var testShuffleEnabled = false
    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        this.testShuffleEnabled = shuffleModeEnabled
        updateListeners { it.onShuffleModeEnabledChanged(testShuffleEnabled) }
    }

    override fun getShuffleModeEnabled(): Boolean {
        return testShuffleEnabled
    }

    override fun isLoading(): Boolean {
        TODO("Not yet implemented")
    }

    override fun seekToDefaultPosition() {
        TODO("Not yet implemented")
    }

    override fun seekToDefaultPosition(mediaItemIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun seekTo(positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun seekTo(mediaItemIndex: Int, positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun getSeekBackIncrement(): Long {
        TODO("Not yet implemented")
    }

    override fun seekBack() {
        TODO("Not yet implemented")
    }

    override fun getSeekForwardIncrement(): Long {
        TODO("Not yet implemented")
    }

    override fun seekForward() {
        TODO("Not yet implemented")
    }

    override fun hasPrevious(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPreviousWindow(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPreviousMediaItem(): Boolean {
        TODO("Not yet implemented")
    }

    override fun previous() {
        TODO("Not yet implemented")
    }

    override fun seekToPreviousWindow() {
        TODO("Not yet implemented")
    }

    override fun seekToPreviousMediaItem() {
        TODO("Not yet implemented")
    }

    override fun getMaxSeekToPreviousPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun seekToPrevious() {
        TODO("Not yet implemented")
    }

    override fun hasNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasNextWindow(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasNextMediaItem(): Boolean {
        TODO("Not yet implemented")
    }

    override fun next() {
        TODO("Not yet implemented")
    }

    override fun seekToNextWindow() {
        TODO("Not yet implemented")
    }

    override fun seekToNextMediaItem() {
        TODO("Not yet implemented")
    }

    override fun seekToNext() {
        TODO("Not yet implemented")
    }

    private var testPlaybackParameters = PlaybackParameters.DEFAULT
    override fun setPlaybackParameters(playbackParameters: PlaybackParameters) {
        this.testPlaybackParameters = playbackParameters
        updateListeners { it.onPlaybackParametersChanged(testPlaybackParameters) }
    }

    override fun setPlaybackSpeed(speed: Float) {
        TODO("Not yet implemented")
    }

    override fun getPlaybackParameters(): PlaybackParameters {
        return testPlaybackParameters
    }

    override fun stop() {
        TODO("Not yet implemented")
    }



    override fun release() {
        TODO("Not yet implemented")
    }

    override fun getCurrentTracks(): Tracks {
        TODO("Not yet implemented")
    }

    override fun getTrackSelectionParameters(): TrackSelectionParameters {
        TODO("Not yet implemented")
    }

    override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) {
        TODO("Not yet implemented")
    }

    private var mediaMetadata : MediaMetadata = MediaMetadata.EMPTY
    override fun getMediaMetadata(): MediaMetadata {
        return mediaMetadata
    }

    fun setMediaMetadata(mediaMetadata: MediaMetadata) {
        this.mediaMetadata = mediaMetadata
        updateListeners { l -> l.onMediaMetadataChanged(mediaMetadata) }
    }

    private var testPlaylistMetadata : MediaMetadata? = null
    override fun getPlaylistMetadata(): MediaMetadata {
        return testPlaylistMetadata ?: MediaMetadata.EMPTY
    }

    override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) {
        this.testPlaylistMetadata = mediaMetadata
        updateListeners { l -> l.onPlaylistMetadataChanged(mediaMetadata) }
    }

    override fun getCurrentManifest(): Any? {
        TODO("Not yet implemented")
    }

    override fun getCurrentTimeline(): Timeline {
        TODO("Not yet implemented")
    }

    override fun getCurrentPeriodIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentMediaItemIndex(): Int {
        return 0
    }

    override fun getNextWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getNextMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getPreviousWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getPreviousMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    var currentTestMediaItem: MediaItem? =null
    fun setCurrentMediaItem(mediaItem: MediaItem) {
        this.currentTestMediaItem = mediaItem
    }
    override fun getCurrentMediaItem(): MediaItem? {
        return currentTestMediaItem
    }

    var testQueue = mutableListOf<MediaItem>()
    override fun getMediaItemCount(): Int {
        return testQueue.size
    }

    override fun getMediaItemAt(index: Int): MediaItem {
        return testQueue[index]
    }

    override fun getDuration(): Long {
        TODO("Not yet implemented")
    }

    var testCurrentPosition = 0L
    override fun getCurrentPosition(): Long {
        return testCurrentPosition
    }

    override fun getBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getBufferedPercentage(): Int {
        TODO("Not yet implemented")
    }

    override fun getTotalBufferedDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun isCurrentWindowDynamic(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemDynamic(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentWindowLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCurrentLiveOffset(): Long {
        TODO("Not yet implemented")
    }

    override fun isCurrentWindowSeekable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemSeekable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlayingAd(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCurrentAdGroupIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentAdIndexInAdGroup(): Int {
        TODO("Not yet implemented")
    }

    override fun getContentDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getAudioAttributes(): AudioAttributes {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Float) {
        TODO("Not yet implemented")
    }

    override fun getVolume(): Float {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurface() {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurface(surface: Surface?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurface(surface: Surface?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurfaceView(surfaceView: SurfaceView?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurfaceView(surfaceView: SurfaceView?) {
        TODO("Not yet implemented")
    }

    override fun setVideoTextureView(textureView: TextureView?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoTextureView(textureView: TextureView?) {
        TODO("Not yet implemented")
    }

    override fun getVideoSize(): VideoSize {
        TODO("Not yet implemented")
    }

    override fun getSurfaceSize(): Size {
        TODO("Not yet implemented")
    }

    override fun getCurrentCues(): CueGroup {
        TODO("Not yet implemented")
    }

    override fun getDeviceInfo(): DeviceInfo {
        TODO("Not yet implemented")
    }

    override fun getDeviceVolume(): Int {
        TODO("Not yet implemented")
    }

    override fun isDeviceMuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setDeviceVolume(volume: Int) {
        TODO("Not yet implemented")
    }

    override fun setDeviceVolume(volume: Int, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun increaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun increaseDeviceVolume(flags: Int) {
        TODO("Not yet implemented")
    }

    override fun decreaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun decreaseDeviceVolume(flags: Int) {
        TODO("Not yet implemented")
    }

    override fun setDeviceMuted(muted: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setDeviceMuted(muted: Boolean, flags: Int) {
        TODO("Not yet implemented")
    }


    fun setPlaybackPositionEvent(onPlaybackPositionChangedEvent: OnPlaybackPositionChangedEvent, playerEvents: Events) {
        this.testCurrentPosition = onPlaybackPositionChangedEvent.currentPosition
        this.isPlayingTestValue = onPlaybackPositionChangedEvent.isPlaying

        updateListeners { it.onEvents(this, playerEvents) }
    }

    fun setPlayerEvents(events: Events) {
        updateListeners { it.onEvents(this, events) }
    }
}