package com.github.goldy1992.mp3player.service

import android.os.Bundle
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RootAuthenticatorTest {
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    private var rootAuthenticator: RootAuthenticator? = null
    @Before
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        rootAuthenticator = RootAuthenticator(mediaItemTypeIds!!)
    }

    @Test
    fun testGetAcceptedId() {
        val expectedMediaId = mediaItemTypeIds!!.getId(MediaItemType.ROOT)
        val args = Bundle()
        args.putString(Constants.PACKAGE_NAME_KEY, PACKAGE_NAME)
        val params = MediaLibraryService.LibraryParams.Builder()
            .setExtras(args)
            .build()
        val result = rootAuthenticator!!.authenticate(params)
        Assert.assertEquals(expectedMediaId, result.value?.mediaId)
    }

    @Test
    fun testGetRejectedId() {
        val packageNameToAccept = StringBuilder()
                .append("myPackage")
                .toString()
        val expectedMediaId = mediaItemTypeIds!!.getId(MediaItemType.ROOT)
        val args = Bundle()
        args.putString(Constants.PACKAGE_NAME_KEY, "rejected")
        val params = MediaLibraryService.LibraryParams.Builder()
            .setExtras(args)
            .build()
        val result = rootAuthenticator!!.authenticate(params)
        Assert.assertEquals(RootAuthenticator.REJECTED_MEDIA_ROOT_ID, result.value?.mediaId)
    }

    @Test
    fun subscribeWithRejectedId() {
        Assert.assertTrue(rootAuthenticator!!.rejectRootSubscription(RootAuthenticator.REJECTED_MEDIA_ROOT_ID))
    }
}