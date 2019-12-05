package com.github.goldy1992.mp3player.service.player;

import com.google.android.exoplayer2.upstream.DataSource;

public class MyDataSourceFactory implements DataSource.Factory {

    private final DataSource dataSource;

    public MyDataSourceFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource createDataSource() {
        return dataSource;
    }
}
