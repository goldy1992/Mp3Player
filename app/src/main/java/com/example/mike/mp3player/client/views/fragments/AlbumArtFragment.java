package com.example.mike.mp3player.client.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentActivity;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.metadata.MetadataListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

public class AlbumArtFragment extends AsyncFragment implements MetadataListener {
    private static final String LOG_TAG = "ALBM_ART_FRGMNT";
    private AlbumArtPainter albumArtPainter;
    private MediaControllerAdapter mediaControllerAdapter;
    private ImageView albumArt;
    private Uri currentUri;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_album_art, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.albumArt = view.findViewById(R.id.albumArt);
        // register listeners
        this.mediaControllerAdapter.registerMetaDataListener(this);
        albumArtPainter.paintOnView(albumArt, mediaControllerAdapter.getCurrentSongAlbumArtUri());
    }

    private void injectDependencies() {
        FragmentActivity activity = getActivity();
        if (activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            MediaActivityCompatComponent component = mediaPlayerActivity.getMediaActivityCompatComponent();
            component.inject(this);
        }
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Inject
    public void setAlbumArtPainter(AlbumArtPainter albumArtPainter) {
        this.albumArtPainter = albumArtPainter;
    }

    @Override
    public void onMetadataChanged(@NonNull MediaMetadataCompat metadata) {
        String albumArtUriPath = metadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        Uri newUri = null;
        try {
            newUri = Uri.parse(albumArtUriPath);
        } catch (NullPointerException ex) {
            Log.e(LOG_TAG, albumArtUriPath + ": is an invalid Uri");
            return;
        }
        /* make a pre check to avoid an unnecessary call to an expensive operation */
        if (!newUri.equals(currentUri)) {
            this.currentUri = newUri;
            albumArtPainter.paintOnView(albumArt, currentUri);
        }

    }

    @VisibleForTesting
    public AlbumArtPainter getAlbumArtPainter() {
        return albumArtPainter;
    }

    public void setCurrentUri(Uri currentUri) {
        this.currentUri = currentUri;
    }
}
