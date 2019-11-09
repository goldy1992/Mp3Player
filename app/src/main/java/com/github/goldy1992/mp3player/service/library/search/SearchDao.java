package com.github.goldy1992.mp3player.service.library.search;

import java.util.List;

public interface SearchDao<T extends SearchEntity> {

    void insert(T t);

    int getCount();

    void insertAll(List<T> songs);

    List<T> query(String query);
}
