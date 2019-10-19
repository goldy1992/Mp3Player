package com.example.mike.mp3player.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

public class AlbumArtPainter {
    private static final String LOG_TAG = "ALBM_ART_PAINTER";
    private final RequestManager requestManager;
    private RequestOptions requestOptions;
    private final Handler mainHandler;
    private final Context context;

    @Inject
    public AlbumArtPainter(Context context, RequestManager requestManager, @Named("main") Handler mainHandler) {
        this.requestManager = requestManager;
        this.mainHandler = mainHandler;
        this.context = context;
        this.requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public void paintOnView(ImageView imageView, @NonNull Uri uri) {

        /* TODO: add an error drawable image for when the album art is not found:
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error() */

        try {
            requestManager.load(uri).apply(requestOptions).fitCenter().into(imageView);
        } catch (Exception ex) {
            // TODO: load a default image when the album art if not found
//            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void clearView(ImageView imageView) {
        imageView.setImageDrawable(null);
    }

    public Context getContext() {
        return context;
    }
}
