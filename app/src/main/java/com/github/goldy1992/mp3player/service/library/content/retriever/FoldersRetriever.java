package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

@Singleton
public class FoldersRetriever extends ContentResolverRetriever {

    @Inject
    public FoldersRetriever(ContentResolver contentResolver,
                            FolderResultsParser resultsParser,
                            @Named("worker") Handler handler) {
        super(contentResolver, resultsParser, handler);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                null, null, null);
    }

    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }

}
