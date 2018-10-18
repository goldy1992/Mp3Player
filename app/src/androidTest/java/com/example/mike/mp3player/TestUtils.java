package com.example.mike.mp3player;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.view.SeekerBar;

import org.hamcrest.Matcher;

public class TestUtils {

    /**
     *
     *
     * @param progress a percentage to progress
     * @return
     */
    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekerBar seekerBar = ((SeekerBar) view);
                int max = seekerBar.getMax();
                int progressValue = (int)((max / 100) * progress);
                seekerBar.setProgress(progressValue);
                seekerBar.getOnSeekBarChangeListener().onStopTrackingTouch(seekerBar);
                //or ((SeekBar) view).setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }


}
