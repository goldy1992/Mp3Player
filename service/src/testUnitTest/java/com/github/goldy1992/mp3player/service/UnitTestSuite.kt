package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.SearchDatabaseModule
import com.github.goldy1992.mp3player.service.library.ContentManagerTest
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilterTest
import com.github.goldy1992.mp3player.service.library.content.filter.SongsFromFolderResultsFilterTest
import com.github.goldy1992.mp3player.service.library.content.observers.AudioObserverTest
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParserTest
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParserTest
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParserTest
import com.github.goldy1992.mp3player.service.library.content.retriever.*
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcherTest
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcherTest
import com.github.goldy1992.mp3player.service.player.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@HiltAndroidTest
@UninstallModules(SearchDatabaseModule::class, MediaSessionCompatModule::class, ContentManagerModule::class)
@Suite.SuiteClasses(
    AudioBecomingNoisyBroadcastReceiverTest::class,
    AudioObserverTest::class,
    ContentManagerTest::class,
    ContentRequestParserTest::class,
    DecreaseSpeedProviderTest::class,
    FolderResultsParserTest::class,
    FoldersRetrieverTest::class,
    FolderSearchResultsFilterTest::class,
    FolderSearcherTest::class,
    IncreaseSpeedProviderTest::class,
    MediaItemFromIdRetrieverTest::class,
    MediaPlaybackServiceTest::class,
    MediaSourceFactoryTest::class,
    MyControlDispatcherTest::class,
    MyDescriptionAdapterTest::class,
    MyMetadataProviderTest::class,
    MyPlaybackPreparerTest::class,
    MyPlayerNotificationManagerTest::class,
    PlaylistManagerTest::class,
    RootAuthenticatorTest::class,
    RootRetrieverTest::class,
    SongFromUriRetrieverTest::class,
    SongResultsParserTest::class,
    SongSearcherTest::class,
    SongsFromFolderResultsFilterTest::class,
    SongsRetrieverTest::class
)
class UnitTestSuite