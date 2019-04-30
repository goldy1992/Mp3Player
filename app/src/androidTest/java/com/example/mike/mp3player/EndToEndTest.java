package com.example.mike.mp3player;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @BeforeClass
    public static void setup() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), SplashScreenEntryActivity.class);
        ActivityScenario.launch(intent);
        Log.i("", "");

    }

    @Test
    public void performTest() {
        Log.i("", "");
    }
}
