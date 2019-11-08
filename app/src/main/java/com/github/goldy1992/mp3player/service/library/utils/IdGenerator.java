package com.github.goldy1992.mp3player.service.library.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class IdGenerator {


    public static final String generateRootId(String prefix) {
        return prefix + RandomStringUtils.randomAlphanumeric(15);
    }



}
