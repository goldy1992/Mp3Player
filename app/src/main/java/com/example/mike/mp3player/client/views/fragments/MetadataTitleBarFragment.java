package com.example.mike.mp3player.client.views.fragments;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.metadata.MetadataListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

public class MetadataTitleBarFragment extends AsyncFragment implements MetadataListener {

    private Toolbar titleBar;
    private MediaControllerAdapter mediaControllerAdapter;
    private TextView trackTitle;
    private TextView artist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_metadata_title_bar, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        // define views
        this.titleBar = view.findViewById(R.id.titleToolbar);
        this.trackTitle = view.findViewById(R.id.songTitle);
        trackTitle.setSelected(true);
        this.artist = view.findViewById(R.id.songArtist);

        // register listeners
        this.mediaControllerAdapter.registerMetaDataListener(this);
        // update views
        this.onMetadataChanged(mediaControllerAdapter.getMetadata());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(titleBar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        @ColorInt int toolbarTextColor = typedValue.data;

        titleBar.getNavigationIcon().setColorFilter(toolbarTextColor, PorterDuff.Mode.SRC_ATOP);
        trackTitle.setTextColor(toolbarTextColor);
        artist.setTextColor(toolbarTextColor);
    }

    private void injectDependencies() {
        MediaActivityCompatComponent component = ((MediaActivityCompat)getActivity())
                .getMediaActivityCompatComponent();
        component.inject(this);
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Override
    public void onMetadataChanged(@NonNull MediaMetadataCompat metadata) {
        mainUpdater.post(() -> {
            this.trackTitle.setText(mediaControllerAdapter.getMetadata().getText(MediaMetadataCompat.METADATA_KEY_TITLE));
            this.artist.setText(mediaControllerAdapter.getMetadata().getText(MediaMetadataCompat.METADATA_KEY_ARTIST));
        });
    }
}
