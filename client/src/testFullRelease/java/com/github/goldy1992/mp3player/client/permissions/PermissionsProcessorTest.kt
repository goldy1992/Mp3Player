package com.github.goldy1992.mp3player.client.permissions

import android.Manifest
import android.content.pm.PackageManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PermissionsProcessorTest {

 //   private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val permissionsGranted= mock<PermissionGranted>()
    private val compatWrapper = mock<CompatWrapper>()
    private lateinit var permissionsProcessor: PermissionsProcessor

    @Before
    fun setup() {
         this.permissionsProcessor = PermissionsProcessor(permissionsGranted, compatWrapper)
    }

    /**
     * Asserts that [PermissionGranted.onPermissionGranted] is called when
     */
    @Test
    fun checkPermissionsWhenAlreadyGrantedTest() {
        whenever(compatWrapper.checkPermissions(anyString()))
            .thenReturn(PackageManager.PERMISSION_GRANTED)

        permissionsProcessor.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        verify(permissionsGranted, times(1)).onPermissionGranted()
        verify(compatWrapper, times(0)).requestPermissions(any(), any())
    }


    /**
     * Asserts that [PermissionGranted.onPermissionGranted] is called when
     */
    @Test
    fun permissionRequestedWhenNotAlreadyGrantedTest() {
        whenever(compatWrapper.checkPermissions(anyString()))
            .thenReturn(PackageManager.PERMISSION_DENIED)

        permissionsProcessor.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        verify(permissionsGranted, times(0)).onPermissionGranted()
        verify(compatWrapper, times(1)).requestPermissions(any(), any())
    }
}