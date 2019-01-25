package com.example.mike.mp3player.commons.library;

public class LibraryId {

    private final Category category;
    private final String id;

    public LibraryId(Category category, String id) {
        this.category = category;
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }
}
