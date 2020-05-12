package com.github.goldy1992.mp3player.service.player

import com.google.android.exoplayer2.upstream.DataSource

class MyDataSourceFactory(private val dataSource: DataSource) : DataSource.Factory {
    override fun createDataSource(): DataSource {
        return dataSource
    }

}