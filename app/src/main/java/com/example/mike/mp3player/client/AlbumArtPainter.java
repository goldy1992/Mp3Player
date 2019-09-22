package com.example.mike.mp3player.client;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Named;

public class AlbumArtPainter {
    private static final String LOG_TAG = "ALBM_ART_PAINTER";
    private final RequestManager requestManager;
    private final Handler mainHandler;

    @Inject
    public AlbumArtPainter(RequestManager requestManager, @Named("main") Handler mainHandler) {
        this.requestManager = requestManager;
        this.mainHandler = mainHandler;
    }

    public void paintOnView(ImageView imageView, Uri uri) {

        /* TODO: add an error drawable image for when the album art is not found:
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error() */

        try {
            RequestBuilder<Drawable> drawableRequestBuilder = requestManager.load(uri).centerCrop();
            mainHandler.post(() -> drawableRequestBuilder.into(imageView));
        } catch (Exception ex) {
            // TODO: load a default image when the album art if not found
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
    }
}
