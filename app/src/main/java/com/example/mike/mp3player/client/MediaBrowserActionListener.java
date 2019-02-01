package com.example.mike.mp3player.client;

import com.example.mike.mp3player.commons.library.Category;

public interface MediaBrowserActionListener {
    void subscribe(Category category, String id);
}
