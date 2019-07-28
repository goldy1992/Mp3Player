package com.example.mike.mp3player.client.views.fragments;

import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.widget.ImageView;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.AlbumArtPainter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AlbumArtFragmentTest extends FragmentTestBase<AlbumArtFragment> {

    @Before
    public void setup() {
        super.setup(AlbumArtFragment.class, true);
    }

    @Test
    public void testOnMetadataLoadedNullUri() {
        FragmentScenario.FragmentAction<AlbumArtFragment> changeMetadata = this::onMetadataChangedNullUri;
        performAction(changeMetadata);
    }

    @Test
    public void testOnMetadataLoadedValidUri() {
        FragmentScenario.FragmentAction<AlbumArtFragment> changeMetadata = this::onMetadataChangedValidUri;
        performAction(changeMetadata);
    }

    @Test
    public void testOnMetadataLoadedSameUri() {
        FragmentScenario.FragmentAction<AlbumArtFragment> changeMetadata = this::onMetadataChangedSameUri;
        performAction(changeMetadata);
    }

    private void onMetadataChangedNullUri(AlbumArtFragment fragment) {
    AlbumArtPainter spiedAlbumArtPainter = spy(fragment.getAlbumArtPainter());
    fragment.setAlbumArtPainter(spiedAlbumArtPainter);
        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ALBUM_ART_URI, null)
                .build();
        fragment.onMetadataChanged(metadata);
        verify(spiedAlbumArtPainter, never()).paintOnView(any(ImageView.class), any(Uri.class));
    }

    private void onMetadataChangedValidUri(AlbumArtFragment fragment) {
        AlbumArtPainter spiedAlbumArtPainter = spy(fragment.getAlbumArtPainter());
        fragment.setAlbumArtPainter(spiedAlbumArtPainter);
        String uri = "uri";
        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ALBUM_ART_URI, uri)
                .build();
        fragment.onMetadataChanged(metadata);
        verify(spiedAlbumArtPainter, times(1)).paintOnView(any(ImageView.class), any(Uri.class));
    }

    private void onMetadataChangedSameUri(AlbumArtFragment fragment) {
        AlbumArtPainter spiedAlbumArtPainter = spy(fragment.getAlbumArtPainter());
        fragment.setAlbumArtPainter(spiedAlbumArtPainter);
        String uriPath = "uri";
        Uri sameUri = Uri.parse(uriPath);
        fragment.setCurrentUri(sameUri);
        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ALBUM_ART_URI, uriPath)
                .build();
        fragment.onMetadataChanged(metadata);
        verify(spiedAlbumArtPainter, never()).paintOnView(any(ImageView.class), any(Uri.class));
    }
}