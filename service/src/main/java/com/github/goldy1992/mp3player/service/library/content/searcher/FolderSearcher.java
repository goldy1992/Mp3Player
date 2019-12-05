package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.VisibleForTesting;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter;
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser;
import com.github.goldy1992.mp3player.service.library.search.Folder;
import com.github.goldy1992.mp3player.service.library.search.FolderDao;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FolderSearcher extends ContentResolverSearcher<Folder> {

    private static final String LIKE_STATEMENT = MediaStore.Audio.Media.DATA + " LIKE ?";

    private final MediaItemTypeIds mediaItemTypeIds;
    @Inject
    public FolderSearcher(ContentResolver contentResolver,
                          FolderResultsParser resultsParser,
                          FolderSearchResultsFilter folderSearchResultsFilter,
                          MediaItemTypeIds mediaItemTypeIds,
                          FolderDao folderDao) {
        super(contentResolver, resultsParser, folderSearchResultsFilter, folderDao);
        this.mediaItemTypeIds = mediaItemTypeIds;
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
    String getIdPrefix() {
        return mediaItemTypeIds.getId(MediaItemType.FOLDER);
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
