package com.example.mike.mp3player;

import android.content.Context;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@Ignore
@RunWith(Runner.class)
public class ExampleInstrumentedTest extends Runner {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mike.mp3player", appContext.getPackageName());
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public void run(RunNotifier notifier) {

    }
}
