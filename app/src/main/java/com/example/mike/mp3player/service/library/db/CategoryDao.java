package com.example.mike.mp3player.service.library.db;

import java.util.List;


public interface CategoryDao<C extends CategoryEntity> {

    List<C> getAll();

}
