package com.example.mike.mp3player.client.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_ALBUM_ART_URI;

public class AlbumArtFragment extends AsyncFragment implements MetaDataListener {

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
        Bundle extras = metadata.getDescription().getExtras();
        if (null != extras) {
            Uri newUri = (Uri) extras.get(META_DATA_ALBUM_ART_URI);
            if (null != newUri && !newUri.equals(currentUri)) {
                this.currentUri = newUri;
                albumArtPainter.paintOnView(albumArt, currentUri);
            }
        }
    }
}
