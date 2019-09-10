package com.example.mike.mp3player.service.library.content;

import android.icu.text.Normalizer2;

import java.text.Normalizer;

public final class SearchQueryParser {

    private Normalizer normalizer;
    public SearchQueryParser() {    }

    public String parseQuery(String query)  {
        return Normalizer.normalize(query, Normalizer.Form.NFD);
    }
}
