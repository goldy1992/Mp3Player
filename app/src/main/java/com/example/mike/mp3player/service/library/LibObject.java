package com.example.mike.mp3player.service.library;

import java.util.Set;

public abstract class LibObject {

    public enum LibObjectType {
        ROOT,
        SONGS,
        SONG,
        FOLDERS,
        FOLDER
    };

    public abstract LibObjectType getParentType();
    public abstract LibObjectType getType();
    public abstract Set<LibObjectType> getChildrenTypes();

}
