package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import static com.example.mike.mp3player.commons.AndroidUtils.getProductFlavor;
import static com.example.mike.mp3player.commons.Constants.AUTOMATION;

public class MediaRetrieverSelector {

    private final Context context;

    public MediaRetrieverSelector(Context context) {
        this.context = context;
    }

    public MediaRetriever getMediaRetriever() {
        return null;
    }

    private void initContentResolver() {
        switch (getProductFlavor()) {
           // case AUTOMATION: return new MockMediaRetriever(context); break;
          //  default: return; new ContentResolverMediaRetriever(context); break;
        }}
}
