package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FolderSearcher extends ContentResolverSearcher {

    public FolderSearcher(ContentResolver contentResolver, ResultsParser resultsParser, FoldersResultFilter foldersResultFilter, String idPrefix) {
        super(contentResolver, resultsParser,  foldersResultFilter, idPrefix);
    }

    @Override
    public Cursor performSearchQuery(String query) {
        final String WHERE = MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE";
        final String[] WHERE_ARGS = { "%" + query + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                WHERE, WHERE_ARGS, null);
    }

    @Override
    public MediaItemType getSearchCategory() {
        return MediaItemType.FOLDERS;
    }

    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }
}
