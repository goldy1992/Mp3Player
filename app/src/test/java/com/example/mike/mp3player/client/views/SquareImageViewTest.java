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

import static com.example.mike.mp3player.client.views.SquareImageView.USE_WIDTH;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SquareImageViewTest {

    private int useWidthOrHeight =  USE_WIDTH;
    private Context context;
    private int width;
    private  int height;

    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void testOnMeasureUsingWidth() {
        this.width = 7;
        this.height = 6;
        SquareImageView squareImageView = createSquareImageView(USE_WIDTH);
        squareImageView.measure(width, height);
        squareImageView.onMeasure(width, height);
        assertEquals(width, squareImageView.getWidth());
        assertEquals(width, squareImageView.getHeight());
    }

    private SquareImageView createSquareImageView(final int useWidthOrHeight) {

        AttributeSet attributeSet = Robolectric.buildAttributeSet()
            .addAttribute(R.attr.useWidthOrHeight, String.valueOf(useWidthOrHeight))
            .build();
        SquareImageView squareImageView = new SquareImageView(context, attributeSet, 0);
     //   SquareImageView spiedSquareImageView = spy(squareImageView);
//        when(spiedSquareImageView.getMeasuredHeight()).thenReturn(height);
//        when(spiedSquareImageView.getMeasuredWidth()).thenReturn(width);

        return squareImageView;
    }
}