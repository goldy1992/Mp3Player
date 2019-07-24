package com.example.mike.mp3player.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileNotFoundException;

import javax.inject.Inject;

public class AlbumArtPainter {

    private final Context context;

    @Inject
    public AlbumArtPainter(Context context) {
        this.context = context;
    }

    public void paintOnView(ImageView imageView, Uri uri) {
        RequestManager requestManager = Glide.with(context);
        RequestOptions requestOptions = new RequestOptions();
        /* TODO: add an error drawable image for when the album art is not found:
        requestOptions.error()
        */

        try {
            RequestBuilder<Drawable> drawableRequestBuilder = requestManager.load(uri);
            drawableRequestBuilder.centerCrop().into(imageView);
        } catch (Exception ex) {
            // TODO: load a default image when the album art if not found
        }
    }
}
