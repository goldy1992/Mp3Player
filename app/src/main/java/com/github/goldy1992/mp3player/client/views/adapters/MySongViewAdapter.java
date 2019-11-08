package com.github.goldy1992.mp3player.client.views.adapters;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder;

import java.util.Collections;
import java.util.List;

import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtUri;


public class MySongViewAdapter extends MyGenericRecycleViewAdapter {
    private final String LOG_TAG = "MY_VIEW_ADAPTER";

    public MySongViewAdapter(AlbumArtPainter albumArtPainter, Handler mainHandler) {
        super(albumArtPainter, mainHandler);
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MediaItemViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);
            vh = new MySongViewHolder(v, albumArtPainter);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        final boolean isSongHolder = holder instanceof MySongViewHolder;
        if (isSongHolder && !isEmptyRecycleView()) {
            MySongViewHolder songViewHolder = (MySongViewHolder) holder;
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaItem song = items.get(holder.getAdapterPosition());
            songViewHolder.bindMediaItem(song);
        }
    }

    @NonNull
    @Override
    public String getSectionText(int position) {
        return items.get(position).getDescription().getTitle().toString().substring(0, 1);
    }

    @NonNull
    @Override
    public List<MediaItem> getPreloadItems(int position) {
        if (position >= items.size()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(items.get(position));
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull MediaItem item) {
        Uri uri = getAlbumArtUri(item);
        return uri != null
            ? Glide.with(albumArtPainter.getContext()).load(uri).fitCenter()
            : null;
    }
}

