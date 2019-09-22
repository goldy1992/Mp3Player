package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.Folder;
import com.example.mike.mp3player.service.library.search.FolderDao;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FolderSearcher extends ContentResolverSearcher<Folder> {

    private static final String LIKE_STATEMENT = MediaStore.Audio.Media.DATA + " LIKE ?";
    public FolderSearcher(ContentResolver contentResolver, ResultsParser resultsParser, FoldersResultFilter foldersResultFilter, String idPrefix, FolderDao folderDao) {
        super(contentResolver, resultsParser,  foldersResultFilter, idPrefix, folderDao);
    }

    @Override
    public Cursor performSearchQuery(String query) {
        List<Folder> results =  searchDatabase.query(query);
        if (results != null && !results.isEmpty()) {

            List<String> ids = new ArrayList<>();
            List<String> likeList = new ArrayList<>();
            List<String> whereArgs = new ArrayList<>();
            for (Folder folder : results) {
                ids.add(folder.getId());
                likeList.add(LIKE_STATEMENT);

            }
            final String WHERE = StringUtils.join(likeList, " OR ") + " COLLATE NOCASE";

            for (int i = 0; i < results.size(); i++) {
                whereArgs.add(likeParam(ids.get(i)));
            }

            final String[] WHERE_ARGS = whereArgs.toArray(new String[ids.size()]);
            return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                    WHERE, WHERE_ARGS, null);
        }
        return null;
    }

    @Override
    public MediaItemType getSearchCategory() {
        return MediaItemType.FOLDERS;
    }

    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }

    @VisibleForTesting
    public String likeParam(String value) {
        return "%" + value + "%";
    }
}
