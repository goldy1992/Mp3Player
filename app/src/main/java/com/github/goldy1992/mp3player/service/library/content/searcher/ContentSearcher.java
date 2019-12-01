package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;

import java.util.List;

public interface ContentSearcher {

    List<MediaItem> search(@NonNull String query);
    MediaItemType getSearchCategory();

}
