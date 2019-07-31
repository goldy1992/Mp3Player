package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.ActionProvider;

import com.example.mike.mp3player.R;

public class MySearchActionProvider extends ActionProvider {


    LayoutInflater layoutInflater;
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public MySearchActionProvider(Context context) {

        super(context);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateActionView() {
        return layoutInflater.inflate(R.layout.layout_custom_search, null);
    }
}
