package com.github.goldy1992.mp3player.service.library.search.managers;

import android.os.Handler;

import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;

import org.mockito.Mock;

public abstract class SearchDatabaseManagerTestBase {

    @Mock
    ContentManager contentManager;

    Handler handler;

    @Mock
    SearchDatabase searchDatabase;

    MediaItemTypeIds mediaItemTypeIds;


    public void setup() {
        this.handler = new Handler();
    }

    public abstract void testInsert();

    public abstract void testReindex();
}
