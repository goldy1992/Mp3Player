package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.client.views.SquareImageView.USE_HEIGHT;
import static com.example.mike.mp3player.client.views.SquareImageView.USE_WIDTH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SquareImageViewTest {

    private Context context;
    private static final int TEST_WIDTH = 6;
    private static final int TEST_HEIGHT = 7;

    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void testOnMeasureUsingWidth() {
        SquareImageView squareImageView = createSquareImageView(USE_WIDTH);
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT);
        verify(squareImageView).setDimensions(TEST_WIDTH);
    }

    @Test
    public void testOnMeasureUsingHeight() {
        SquareImageView squareImageView = createSquareImageView(USE_HEIGHT);
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT);
        verify(squareImageView).setDimensions(TEST_HEIGHT);
    }

    @Test
    public void testOnMeasureUsingInvalidValue() {
        final int invalidValue = 2230;
        SquareImageView squareImageView = createSquareImageView(invalidValue);
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT);
        verify(squareImageView, never()).setDimensions(any());
    }


    private SquareImageView createSquareImageView(final int useWidthOrHeight) {

        AttributeSet attributeSet = Robolectric.buildAttributeSet()
            .addAttribute(R.attr.useWidthOrHeight, "" + useWidthOrHeight)
            .build();
        SquareImageView squareImageView = new SquareImageView(context, attributeSet, 0);
        SquareImageView spiedSquareImageView = spy(squareImageView);
        when(spiedSquareImageView.getMeasuredHeight()).thenReturn(TEST_HEIGHT);
        when(spiedSquareImageView.getMeasuredWidth()).thenReturn(TEST_WIDTH);

        return spiedSquareImageView;
    }
}