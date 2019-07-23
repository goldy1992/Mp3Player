package com.example.mike.mp3player.client;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

public class AlbumArtPainter {

    private final Context context;

    @Inject
    public AlbumArtPainter(Context context) {
        this.context = context;
    }

    public void paintOnView(ImageView imageView, Uri uri) {
        Glide.with(context).load(uri).centerCrop().into(imageView);
    }
}
