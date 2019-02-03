package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrackInfoFragment extends Fragment implements MetaDataListener {

    private TextView artist;
    private TextView track;
    private MediaControllerAdapter mediaControllerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_track_info, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.artist = view.findViewById(R.id.artistName);
        this.track = view.findViewById(R.id.trackName);
    }

    public void init(MediaControllerAdapter adapter) {
        this.mediaControllerAdapter = adapter;
        this.mediaControllerAdapter.registerMetaDataListener(this);
    }

    public TextView getArtist() {
        return artist;
    }

    public TextView getTrack() {
        return track;
    }

    public void setArtist(String artist) {
        this.artist.setText(getString(R.string.ARTIST_NAME, artist));
    }

    public void setTrack(String track) {
        this.track.setText(getString(R.string.TRACK_NAME, track));
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metaData) {
        setArtist(metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        setTrack(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
    }
}
