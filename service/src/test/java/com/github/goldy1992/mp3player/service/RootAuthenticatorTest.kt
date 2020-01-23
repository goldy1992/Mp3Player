package com.github.goldy1992.mp3player.service

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
        val packageNameToAccept: String = StringBuilder()
                .append("myPackage")
                .append(PACKAGE_NAME)
                .toString()
        val result = rootAuthenticator!!.authenticate(packageNameToAccept, 0, null)
        Assert.assertEquals(expectedMediaId, result.rootId)
    }

    @Test
    fun testGetRejectedId() {
        val packageNameToAccept = StringBuilder()
                .append("myPackage")
                .toString()
        val result = rootAuthenticator!!.authenticate(packageNameToAccept, 0, null)
        Assert.assertEquals(RootAuthenticator.REJECTED_MEDIA_ROOT_ID, result.rootId)
    }

    @Test
    fun subscribeWithRejectedId() {
        Assert.assertTrue(rootAuthenticator!!.rejectRootSubscription(RootAuthenticator.REJECTED_MEDIA_ROOT_ID))
    }
}