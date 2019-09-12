package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.Folder;
import com.example.mike.mp3player.service.library.search.SearchDatabase;
import com.example.mike.mp3player.service.library.search.Song;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FolderSearcher extends ContentResolverSearcher {

    private static final String LIKE_STATEMENT = MediaStore.Audio.Media.DATA + " LIKE ?";
    public FolderSearcher(ContentResolver contentResolver, ResultsParser resultsParser, FoldersResultFilter foldersResultFilter, String idPrefix, SearchDatabase searchDatabase) {
        super(contentResolver, resultsParser,  foldersResultFilter, idPrefix, searchDatabase);
    }

    @Override
    public Cursor performSearchQuery(String query) {
        String searchQuery = StringUtils.stripAccents(query);
        List<Folder> results =  searchDatabase.folderDao().query(searchQuery);
        List<String> ids = new ArrayList<>();
        if (results != null && !results.isEmpty()) {
            for (Folder folder : results) {
                ids.add(folder.getId());
            }
        }
        List<String> likeList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            likeList.add(LIKE_STATEMENT);
        }

        final String WHERE = StringUtils.join(likeList, " OR ") + " COLLATE NOCASE";


        List<String> whereArgs = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            whereArgs.add("%" + ids.get(i) + "%");
        }
        final String[] WHERE_ARGS = whereArgs.toArray(new String[ids.size()]);
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
