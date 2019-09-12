package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.Folder;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FoldersRetriever extends ContentResolverRetriever {

    public FoldersRetriever(ContentResolver contentResolver, ResultsParser resultsParser,
                            SearchDatabase searchDatabase, Handler handler) {
        super(contentResolver, resultsParser, searchDatabase, handler);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                null, null, null);
    }

    @Override
    void updateSearchDatabase(List<MediaBrowserCompat.MediaItem> results) {
        handler.post(() -> {
            final int resultsSize = results.size();
            final int count = searchDatabase.folderDao().getCount();

            if (count != resultsSize) { // INSERT NORMALISED VALUES
                List<Folder> folders = new ArrayList<>();
                for (MediaBrowserCompat.MediaItem mediaItem : results) {
                    Folder folder = new Folder(MediaItemUtils.getDirectoryPath(mediaItem));
                    folder.setName(StringUtils.stripAccents(MediaItemUtils.getDirectoryName(mediaItem)));
                    folders.add(folder);
                }
                searchDatabase.folderDao().insertAll(folders);
            }
        });
    }

    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }

}
