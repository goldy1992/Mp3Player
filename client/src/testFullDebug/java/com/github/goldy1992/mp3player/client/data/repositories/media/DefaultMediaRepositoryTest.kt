package com.github.goldy1992.mp3player.client.data.repositories.media

import com.github.goldy1992.mp3player.client.data.sources.FakeMediaDataSource
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import org.junit.Assert.*
import org.junit.Before

class DefaultMediaRepositoryTest {

    private val fakeMediaDataSource : MediaDataSource = FakeMediaDataSource()

    private val defaultMediaRepository : DefaultMediaRepository = DefaultMediaRepository(fakeMediaDataSource)

    @Before
    fun setup() {

    }


}