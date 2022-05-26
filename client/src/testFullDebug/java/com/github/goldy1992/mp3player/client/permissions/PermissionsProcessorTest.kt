package com.github.goldy1992.mp3player.client.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import org.checkerframework.checker.units.qual.A
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

    private val launcher : ActivityResultLauncher<String> = mock()
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

        val expectedPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permissionsProcessor.requestPermission(expectedPermission,
            launcher)
        verify(permissionsGranted, times(1)).onPermissionGranted()
        verify(launcher, never()).launch(anyString())
    }


    /**
     * Asserts that [PermissionGranted.onPermissionGranted] is called when
     */
    @Test
    fun permissionRequestedWhenNotAlreadyGrantedTest() {
        whenever(compatWrapper.checkPermissions(anyString()))
            .thenReturn(PackageManager.PERMISSION_DENIED)

        val expectedPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permissionsProcessor.requestPermission(expectedPermission, launcher)
        verify(permissionsGranted, times(0)).onPermissionGranted()
        verify(launcher, times(1)).launch(expectedPermission)
    }

    class MockResultsLauncher : ActivityResultLauncher<String>() {
        override fun launch(input: String?, options: ActivityOptionsCompat?) {

        }

        override fun unregister() {

        }

        override fun getContract(): ActivityResultContract<String, *> {
        return ActivityResultContracts.RequestPermission()
        }
    }
}

