package com.example.mike.mp3player.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Named;

public class AlbumArtPainter {
    private static final String LOG_TAG = "ALBM_ART_PAINTER";
    private final Context context;
    private final Handler mainHandler;

    @Inject
    public AlbumArtPainter(Context context, @Named("main") Handler mainHandler) {
        this.context = context;
        this.mainHandler = mainHandler;
    }

    public void paintOnView(ImageView imageView, Uri uri) {
        RequestManager requestManager = Glide.with(context);
        RequestOptions requestOptions = new RequestOptions();
        /* TODO: add an error drawable image for when the album art is not found:
        requestOptions.error()
        */

        try {
            RequestBuilder<Drawable> drawableRequestBuilder = requestManager.load(uri);
            mainHandler.post(() -> drawableRequestBuilder.centerCrop().into(imageView));
            Debug.stopMethodTracing();
        } catch (Exception ex) {
            // TODO: load a default image when the album art if not found
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
    }
}
