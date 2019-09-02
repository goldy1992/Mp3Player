package com.example.mike.mp3player.client.views.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.test.platform.app.InstrumentationRegistry;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecyclerViewAdapterTestBase {

    private Context context;
    ViewGroup viewGroup;

    public void setup() {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.viewGroup = getMockViewGroup();
    }

    private ViewGroup getMockViewGroup() {
        ViewGroup viewGroup = mock(ViewGroup.class);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        when(viewGroup.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        when(viewGroup.getContext()).thenReturn(this.context);
        return viewGroup;
    }
}
