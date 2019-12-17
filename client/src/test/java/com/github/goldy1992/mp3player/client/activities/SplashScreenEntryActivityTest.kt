package com.github.goldy1992.mp3player.client.activities

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SplashScreenEntryActivityTest {

    private lateinit var scenario: ActivityScenario<SplashScreenEntryActivityInjectorTestImpl>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(SplashScreenEntryActivityInjectorTestImpl::class.java)
    }

    @Test
    fun testNonRootTask() {
        scenario.onActivity { splashActivity: SplashScreenEntryActivity ->
            val spiedActivity = spy(splashActivity)
            val context = InstrumentationRegistry.getInstrumentation().context
            val intent = Intent(context, SplashScreenEntryActivity::class.java)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.action = Intent.ACTION_MAIN
            whenever(spiedActivity.intent).thenReturn(intent)
            whenever(spiedActivity.isTaskRoot).thenReturn(false)
            spiedActivity.onCreate(null)
            Assert.assertTrue(splashActivity.isFinishing)
        }
    }

    @Test
    fun testOnRequestPermissionResult() {
        Assert.assertTrue(true)
    }

    @Test
    fun testOnPermissionGranted() {
        scenario!!.onActivity { splashActivity: SplashScreenEntryActivity ->
            val spiedActivity = spy(splashActivity)
            doNothing().whenever(spiedActivity).runOnUiThread(any<Runnable>())
            spiedActivity.onPermissionGranted()
            Assert.assertTrue(spiedActivity.isPermissionGranted)
        }
    }

    @Test
    fun testOnActivityResultValidAppTerminated() {
        scenario!!.onActivity { splashActivity: SplashScreenEntryActivity ->
            val spiedActivity = spy(splashActivity)
            spiedActivity.onActivityResult(SplashScreenEntryActivity.APP_TERMINATED, SplashScreenEntryActivity.APP_TERMINATED, null)
            verify(spiedActivity, times(1)).finish()
        }
    }

    @Test
    fun testOnActivityResultInvalidAppTerminated() {
        scenario!!.onActivity { splashActivity: SplashScreenEntryActivity ->
            val NOT_APP_TERMINATED = -1
            val spiedActivity = spy(splashActivity)
            spiedActivity.onActivityResult(NOT_APP_TERMINATED, NOT_APP_TERMINATED, null)
            verify(spiedActivity, never()).finish()
        }
    }

    @Test
    fun testOnRequestPermissionsResultAccepted() {
        scenario.onActivity { splashActivity: SplashScreenEntryActivity ->
            var splashScreenActivitySpied : SplashScreenEntryActivity = spy(splashActivity)
            doNothing().whenever(splashScreenActivitySpied).onPermissionGranted()
            val requestCode = 200
            val permissions = arrayOf(permission.WRITE_EXTERNAL_STORAGE)
            val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
            splashScreenActivitySpied.onRequestPermissionsResult(requestCode, permissions, grantResults)
            verify(splashScreenActivitySpied, times(1)).onPermissionGranted()
        }
    }

    @Test
    fun testOnRequestPermissionsResultRejected() {
        scenario!!.onActivity { splashActivity: SplashScreenEntryActivity ->
            val requestCode = 200
            val permissions = arrayOf(permission.WRITE_EXTERNAL_STORAGE)
            val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)
            splashActivity.onRequestPermissionsResult(requestCode, permissions, grantResults)
            Assert.assertTrue(splashActivity.isFinishing)
        }
    }

    @Test
    fun testOnRequestPermissionsResultEmpty() {
        scenario!!.onActivity { splashActivity: SplashScreenEntryActivity ->
            val requestCode = 200
            val permissions = arrayOf<String>()
            val grantResults = intArrayOf()
            splashActivity.onRequestPermissionsResult(requestCode, permissions, grantResults)
            Assert.assertTrue(splashActivity.isFinishing)
        }
    } //    @After
//    public void tearDown() {
//        if (null != splashScreenEntryActivity) {
//            Thread thread = splashScreenEntryActivity.getThread();
//            if (null != thread) {
//                thread.stop();
//            }
//        }
//    }
}