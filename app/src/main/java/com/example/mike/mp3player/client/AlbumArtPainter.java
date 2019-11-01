package com.example.mike.mp3player.client;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.FixedPreloadSizeProvider;

import javax.inject.Inject;

public class AlbumArtPainter {
    private static final String LOG_TAG = "ALBM_ART_PAINTER";
    private final RequestManager requestManager;
    private RequestOptions requestOptions;
    private final Context context;

    @Inject
    public AlbumArtPainter(Context context, RequestManager requestManager) {
        this.requestManager = requestManager;
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


    public void paintOnView(ImageView imageView, @NonNull byte[] image) {
        try {
            requestManager.load(image).apply(requestOptions).fitCenter().into(imageView);
        } catch (Exception ex) {
            // TODO: load a default image when the album art if not found
//            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
    }

    public RecyclerViewPreloader<MediaItem> createPreloader(ListPreloader.PreloadModelProvider<MediaItem> preloadModelProvider) {
        FixedPreloadSizeProvider<MediaItem> preloadSizeProvider = new FixedPreloadSizeProvider<>(20, 20);
        return new RecyclerViewPreloader<>(
                Glide.with(context), preloadModelProvider, preloadSizeProvider, 10 /*maxPreload*/);
    }

    public void clearView(ImageView imageView) {
        imageView.setImageDrawable(null);
    }

    public Context getContext() {
        return context;
    }
}
