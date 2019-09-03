package com.example.mike.mp3player.commons;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class MediaItemUtilsTest {

    @Test
    public void testGetExtrasNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder()
                .setMediaId("anId")
                .build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getExtras(mediaItem));
    }

    @Test
    public void testGetExtrasNotNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle("algo") // sets an extra
                .build();
        Bundle result = MediaItemUtils.getExtras(mediaItem);
        assertNotNull(result);
    }

    @Test
    public void testGetMediaIdNotNull() {
        final String mediaId = "MEDIA_ID";
        MediaItem mediaItem = new MediaItemBuilder(mediaId).build();
        assertEquals(mediaId, MediaItemUtils.getMediaId(mediaItem));
    }

    @Test
    public void testGetTitleNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(null)
                .build();
        assertNull(MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    public void testGetTitleNotNull() {
        final String title = "TITLE";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(title)
                .build();
        assertEquals(title, MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    public void testGetArtistNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setArtist(null)
                .build();
        assertNull(MediaItemUtils.getArtist(mediaItem));
    }

    @Test
    public void testGetArtistNotNull() {
        final String artist = "ARTIST";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setArtist(artist)
                .build();
        assertEquals(artist, MediaItemUtils.getArtist(mediaItem));
    }

    @Test
    public void testGetAlbumArtPathNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setAlbumArtUri(null)
                .build();
        assertNull(MediaItemUtils.getAlbumArtPath(mediaItem));
    }

    @Test
    public void testGetAlbumArtPathNotNull() {
        final String expectedPath = "sdsad";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setAlbumArtUri(Uri.parse(expectedPath))
                .build();
        assertEquals(expectedPath, MediaItemUtils.getAlbumArtPath(mediaItem));
    }

    @Test
    public void testGetDirectoryNameNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .build();
        assertNull(MediaItemUtils.getDirectoryName(mediaItem));
    }

    @Test
    public void testGetDirectoryNameNotNull() {
        final String expectedPath = "sdsad";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setFile(new File("/"+expectedPath + "/child"))
                .build();
        assertEquals(expectedPath, MediaItemUtils.getDirectoryName(mediaItem));
    }

    @Test
    public void testGetMediaUri() {
        final Uri uri = mock(Uri.class);
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaUri(uri)
                .build();
        Uri result = MediaItemUtils.getMediaUri(mediaItem);
        assertEquals(uri, result);
    }

    @Test
    public void testGetDuration() {
        final long expectedDuration = 231L;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDuration(expectedDuration)
                .build();
        long result = MediaItemUtils.getDuration(mediaItem);
        assertEquals(expectedDuration, result);
    }

    @Test
    public void testGetMediaItemType() {
        final MediaItemType expectedMediaItemType = MediaItemType.SONGS;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaItemType(expectedMediaItemType)
                .build();
        MediaItemType result = MediaItemUtils.getMediaItemType(mediaItem);
        assertEquals(expectedMediaItemType, result);
    }
    @Test
    public void testGetRootItemType() {
        final MediaItemType expectedMediaItemType = MediaItemType.FOLDER;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setRootItemType(expectedMediaItemType)
                .build();
        MediaItemType result = MediaItemUtils.getRootMediaItemType(mediaItem);
        assertEquals(expectedMediaItemType, result);
    }

    @Test
    public void testGetLibraryId() {
        final String expectedLibraryId = "libraryId";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setLibraryId(expectedLibraryId)
                .build();
        String result = MediaItemUtils.getLibraryId(mediaItem);
        assertEquals(expectedLibraryId, result);
    }

//    @Test
//    public void testExtractTitleValidTitle() {
//        final String expectedTitle = "title";
//        MediaItem mediaItem = new MediaItemBuilder("id")
//                .setTitle(expectedTitle)
//                .build();
//        assertEquals(expectedTitle, MediaItemUtils.extractTitle(mediaItem));
//
//    }
//
//    @Test
//    public void testExtractTitleNullTitleNoFileName() {
//        final String expectedTitle = UNKNOWN;
//        MediaItem mediaItem = new MediaItemBuilder("id")
//                .setTitle(null)
//                .build();
//        assertEquals(expectedTitle, MediaItemUtils.extractTitle(mediaItem));
//    }
//
//    @Test
//    public void testExtractTitleNullTitleFileNameNoExtension() {
//        final String expectedTitle = "no_extension";
//        MediaItem mediaItem = new MediaItemBuilder("id")
//                .setTitle(null)
//                .setFileName(expectedTitle)
//                .build();
//        assertEquals(expectedTitle, MediaItemUtils.extractTitle(mediaItem));
//    }
//
//    @Test
//    public void testExtractTitleNullTitleFileNameWithExtension() {
//
//        final String expectedTitle = "file_with_extension";
//        final String fileName =  expectedTitle + ".mp3";
//                MediaItem mediaItem = new MediaItemBuilder("id")
//                .setTitle(null)
//                .setFileName(fileName)
//                .build();
//        assertEquals(expectedTitle, MediaItemUtils.extractTitle(mediaItem));
//    }

    @Test
    public void testGetDescriptionNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDescription(null)
                .build();
        assertNull(MediaItemUtils.getDescription(mediaItem));
    }

    @Test
    public void testGetDescriptionNotNull() {
        final String description = "description";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDescription(description)
                .build();
        assertEquals(description, MediaItemUtils.getDescription(mediaItem));
    }

}