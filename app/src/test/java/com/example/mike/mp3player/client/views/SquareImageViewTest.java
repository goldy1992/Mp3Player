package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.AttributeSetBuilderImpl;

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
        SquareImageView squareImageView = createSquareImageView("X");
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT);
        verify(squareImageView).setDimensions(TEST_WIDTH);
    }

    @Test
    public void testOnMeasureUsingHeight() {
        SquareImageView squareImageView = createSquareImageView("Y");
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT);
        verify(squareImageView).setDimensions(TEST_HEIGHT);
    }

    private SquareImageView createSquareImageView(String useWidthOrHeight) {
        AttributeSetBuilderImpl.ArscResourceResolver resourceResolver = new AttributeSetBuilderImpl.ArscResourceResolver(context);
        MyAttributeSetBuilderImpl myAttributeSetBuilder = new MyAttributeSetBuilderImpl(resourceResolver);
        myAttributeSetBuilder.addAttribute(R.attr.useWidthOrHeight, useWidthOrHeight);
        AttributeSet attributeSet = myAttributeSetBuilder.build();
        SquareImageView squareImageView = new SquareImageView(context, attributeSet, 0);
        SquareImageView spiedSquareImageView = spy(squareImageView);
        when(spiedSquareImageView.getMeasuredHeight()).thenReturn(TEST_HEIGHT);
        when(spiedSquareImageView.getMeasuredWidth()).thenReturn(TEST_WIDTH);

        return spiedSquareImageView;
    }
}