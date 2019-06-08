package com.example.mike.mp3player.service;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ServiceManagerTest {

    private static final String LOG_TAG = "SERVICE_MANAGER_TEST";
    private ServiceManager serviceManager;
    @Mock
    private MediaSessionAdapter mediaSession;
    @Mock
    private MediaPlaybackService mediaPlaybackService;
    @Mock
    private MyNotificationManager notificationManager;

    /**
     * setup method
     * @throws IllegalAccessException IllegalAccessException
     */
    @Before
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        when(mediaPlaybackService.getApplicationContext()).thenReturn(context);
        when(notificationManager.getNotificationManager()).thenReturn(mock(NotificationManager.class));
        this.serviceManager = new ServiceManager(mediaPlaybackService, mediaSession);
        FieldUtils.writeField(serviceManager, "notificationManager", notificationManager, true);
        assertServiceStarted(false);
    }
    @Test
    public void testStartService() {
        serviceManager.startService();
        assertServiceStarted(true);
    }
    @Test
    public void testPauseService() {
        serviceManager.pauseService();
        assertServiceStarted(false);
    }
    /**
     * util method to read the service started field and assert what is expected
     * @param expected true if true is expected, false otherwise
     */
    private void assertServiceStarted(boolean expected) {
        try {
            boolean isStarted = (boolean)FieldUtils.readField(serviceManager, "serviceStarted", true);
            if (expected) {
                assertTrue(isStarted);
            } else {
                assertFalse(isStarted);
            }
        }
        catch (IllegalAccessException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append(ExceptionUtils.getFullStackTrace(ex));
            sb.append("\nFAILING TEST");
            Log.e(LOG_TAG, sb.toString());
            fail();
        }
    }
}