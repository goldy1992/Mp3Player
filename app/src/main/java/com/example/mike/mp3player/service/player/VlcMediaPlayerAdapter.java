//package com.example.mike.mp3player.service;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.media.AudioAttributes;
//import android.media.AudioFocusRequest;
//import android.media.AudioManager;
//import android.media.PlaybackParams;
//import android.net.Uri;
//import android.os.Build;
//import androidx.annotation.RequiresApi;
//import androidx.annotation.VisibleForTesting;
//
//import android.support.v4.media.MediaMetadataCompat;
//import android.support.v4.media.session.PlaybackStateCompat;
//import android.util.Log;
//
//import com.example.mike.mp3player.commons.AndroidUtils;
//
//import org.videolan.libvlc.LibVLC;
//import org.videolan.libvlc.LibVlcUtil;
//import org.videolan.libvlc.Media;
//import org.videolan.libvlc.MediaList;
//import org.videolan.libvlc.MediaPlayer;
//import org.videolan.libvlc.util.AndroidUtil;
//import org.videolan.libvlc.util.Extensions;
//import org.videolan.libvlc.util.MediaBrowser;
//import org.videolan.libvlc.util.VLCUtil;
//
//import java.io.IOException;
//
//public class VlcMediaPlayerAdapter implements LibVLC.OnNativeCrashListener {
//
//    private static final float DEFAULT_SPEED = 1.0f;
//    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
//    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
//    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
//    private MediaPlayer mediaPlayer;
//    private AudioManager.OnAudioFocusChangeListener afChangeListener;
//    private Context context;
//    /**
//     * initialise to paused so the player doesn't start playing immediately
//     */
//    @PlaybackStateCompat.State
//    private int currentState = PlaybackStateCompat.STATE_PAUSED;;
//    private float currentPlaybackSpeed = DEFAULT_SPEED;
//    private boolean isPrepared = false;
//    private LibVLC libVLC;
//
//    public VlcMediaPlayerAdapter(Context context) {
//        this.context = context;
//    }
//
//    /**
//     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
//     * crashing
//     */
//    public void reset(Uri uri) {
//        Log.i(LOG_TAG, "compatible cpu: " + VLCUtil.hasCompatibleCPU(context));
//        resetPlayer();
//        prepareFromUri(uri);
//
//        this.afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//            @Override
//            public void onAudioFocusChange(int i) {
//            }
//        };
//    }
//
//    public void play() {
//        if (!prepare()) {
//            return;
//        }
//
//        if (requestAudioFocus() || isPlaying()) {
//            try {
//                // Set the session active  (and update metadata and state)
//                currentState = PlaybackStateCompat.STATE_PLAYING;
//                // start the player (custom call)
//                getCurrentMediaPlayer().play();
//               // updatePlaybackParameters();
//                //            // Register BECOME_NOISY BroadcastReceiver
////            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public boolean prepareFromUri(Uri uri) {
//        if (null != uri)
//        {
//            try {
//                resetPlayer();
//                setCurrentUri(uri);
//                prepare();
//              //  updatePlaybackParameters();
//                return true;
//            } catch (Exception ex) {
//                Log.e(LOG_TAG, ex.getMessage());
//                return false;
//            }
//        }
//        return false;
//    }
//
//    private void resetPlayer() {
//        if (null != getCurrentMediaPlayer()) {
//            getCurrentMediaPlayer().release();
//        } else {
//            this.libVLC = new LibVLC();
//            this.mediaPlayer = new MediaPlayer(libVLC);
//
//        }
//        this.isPrepared = false;
//    }
//
//    /**
//     * we never want to use stop when calling the player,
//     * because we can just reset the mediaplayer and when a song is
//     * prepared we can put it into the paused state.
//     */
//    @Deprecated
//    public void stop() {
//        if (!isPrepared()) {
//            return;
//        }
//        // unregisterReceiver(myNoisyAudioStreamReceiver);
//        currentState= PlaybackStateCompat.STATE_STOPPED;
//        isPrepared = false;
//        getCurrentMediaPlayer().stop();
//        resetPlayer();
//        // Take the service out of the foreground
//    }
//
//    public void pause() {
//        if (!isPrepared() || isPaused()) {
//            return;
//        }
//        // Update metadata and state
//        getCurrentMediaPlayer().pause();
//        currentState = PlaybackStateCompat.STATE_PAUSED;
//    }
//
//    public void increaseSpeed(float by) {
//        float currentSpeed = getCurrentMediaPlayer().getRate();
//        float newSpeed = currentSpeed + by;
//        if (validSpeed(newSpeed)) {
//            mediaPlayer.setRate(newSpeed);
//            this.currentPlaybackSpeed = newSpeed;
////            updatePlaybackParameters();
//        }
//    }
//
//    public void decreaseSpeed(float by) {
//        float currentSpeed = getCurrentMediaPlayer().getRate();
//        float newSpeed = currentSpeed - by;
//        if (validSpeed(newSpeed)) {
//            mediaPlayer.setRate(newSpeed);
//            this.currentPlaybackSpeed = newSpeed;
////            updatePlaybackParameters();
//        }
//    }
//
//    public void seekTo(long position) {
//        if (!prepare()) {
//            return;
//        }
//        getCurrentMediaPlayer().setTime(position);
//    }
//
//    private boolean requestAudioFocus() {
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        return AndroidUtils.isAndroidOreoOrHigher() ?
//                requestAudioFocusOreo(am)  :
//                requestAudioFocusBelowApi26(am);
//    }
//
//    private void setCurrentUri(Uri uri) throws Exception {
//        LibVLC.setOnNativeCrashListener(this);
//        Log.i(LOG_TAG, LibVlcUtil.getMachineSpecs().toString());
//        mediaPlayer.getVLCVout();
//            Media media = new Media(this.libVLC, uri);
//            media.setHWDecoderEnabled(true, false);
//
//       // MediaBrowser mediaBrowser = new MediaBrowser();
//        //mediaBrowser.
//            mediaPlayer.setMedia(media);
//            media.release();
//    }
//
//    private boolean prepare() {
//        if (!isPrepared()) {
//               currentState = PlaybackStateCompat.STATE_PAUSED;
//                isPrepared = true;
//        }
//        return isPrepared();
//    }
//
////    private void updatePlaybackParameters() {
////        if (currentState == PlaybackStateCompat.STATE_PLAYING) {
////            if (getCurrentMediaPlayer() != null && getCurrentMediaPlayer().getPlaybackParams() != null) {
////                if (validSpeed(currentPlaybackSpeed)) {
////                    PlaybackParams newparams = mediaPlayer.getPlaybackParams().setSpeed(currentPlaybackSpeed);
////                    mediaPlayer.setPlaybackParams(newparams);
////                    //logPlaybackParams(mediaPlayer.getPlaybackParams());
////                }
////            }
////        }
////    }
//
//    public MediaPlayer getCurrentMediaPlayer() {
//        return mediaPlayer;
//    }
//
//    public PlaybackStateCompat getMediaPlayerState() {
//        return new PlaybackStateCompat.Builder()
//                .setState(getCurrentState(),
//                        (long)mediaPlayer.getMediaPlayerPosition(),
//                        getCurrentPlaybackSpeed())
//                .build();
//    }
//
//    public MediaMetadataCompat.Builder getCurrentMetaData() {
//        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
//        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getMedia().getDuration());
//    }
//
//    public int getCurrentTrackPosition() {
//        return (int)mediaPlayer.getMediaPlayerPosition();
//    }
//
//    public int getCurrentState() {
//        return currentState;
//    }
//
//    @TargetApi(Build.VERSION_CODES.FROYO)
//    @SuppressWarnings("deprecation")
//    private boolean requestAudioFocusBelowApi26( AudioManager am) {
//
//        // Request audio focus for playback, this registers the afChangeListener
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(afChangeListener,
//                // Use the music stream.
//                AudioManager.STREAM_MUSIC,
//                // Request permanent focus.
//                AudioManager.AUDIOFOCUS_GAIN);
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private boolean requestAudioFocusOreo( AudioManager am) {
//
//        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
//                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
//        AudioAttributes audioAttributes = audioAttributesBuilder.build();
//        // Request audio focus for playback, this registers the afChangeListener
//        AudioFocusRequest.Builder audioFocusRequestBuilder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//                .setOnAudioFocusChangeListener(afChangeListener)
//                .setAudioAttributes(audioAttributes)
//                .setAcceptsDelayedFocusGain(true)
//                .setWillPauseWhenDucked(false);
//
//        AudioFocusRequest audioFocusRequest = audioFocusRequestBuilder.build();
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(audioFocusRequest);
//    }
//
//    public boolean isPrepared() {
//        return isPrepared;
//    }
//
//    public boolean isPlaying() {
//        return currentState == PlaybackStateCompat.STATE_PLAYING;
//    }
//
//    public boolean isPaused() {
//        return currentState == PlaybackStateCompat.STATE_PAUSED;
//    }
//
//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
//    public void setMediaPlayer(MediaPlayer mediaPlayer) {
//        this.mediaPlayer = mediaPlayer;
//    }
//
//    /**
//     * reset playbackspeed to be paused i.e 0.0f
//     */
//    public float getCurrentPlaybackSpeed() {
//        return currentPlaybackSpeed;
//    }
//
//    private boolean validSpeed(float speed) {
//        return speed >= MINIMUM_PLAYBACK_SPEED &&
//                speed <= MAXIMUM_PLAYBACK_SPEED;
//    }
//
//    private void logPlaybackParams(PlaybackParams playbackParams) {
//        StringBuilder sb = new StringBuilder();
//
//        String pitch = "pitch: " + playbackParams.getPitch()+ "\n";
//        String audioFallbackMode = "audiofallbackmode: ";
//        switch (playbackParams.getAudioFallbackMode()) {
//            case PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_DEFAULT"; break;
//            case PlaybackParams.AUDIO_FALLBACK_MODE_FAIL: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_FAIL"; break;
//            case PlaybackParams.AUDIO_FALLBACK_MODE_MUTE: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_MUTE"; break;
//            default: audioFallbackMode = "none";
//        }
//        audioFallbackMode += "\n";
//        String speed = "speed: " + playbackParams.getSpeed() + "\n";
//
//        String log = sb.append(pitch).append(audioFallbackMode).append(speed).toString();
//        Log.d(LOG_TAG, log);
//    }
//
//    @Override
//    public void onNativeCrash() {
//        Log.i(LOG_TAG, "native crashed");
//    }
//}
