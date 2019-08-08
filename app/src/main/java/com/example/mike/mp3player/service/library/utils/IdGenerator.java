package com.example.mike.mp3player.service.library.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class IdGenerator {

    public static final String generateRootId(String prefix) {
        return prefix + RandomStringUtils.randomAlphanumeric(15);
    }

    public static String generateId(String parentId, String mediaId) {
        return new StringBuilder()
                .append(parentId)
                .append("\\|")
                .append(mediaId)
                .toString();
    }


}
