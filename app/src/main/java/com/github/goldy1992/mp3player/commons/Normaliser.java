package com.github.goldy1992.mp3player.commons;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

public final class Normaliser {

    public static String normalise(@NonNull String query) {
        query = StringUtils.stripAccents(query);
        return query.trim().toUpperCase();
    }
}
