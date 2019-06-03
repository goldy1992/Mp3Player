//package com.example.mike.mp3player.service.library;
//
//import android.content.Context;
//
//import com.example.mike.mp3player.service.library.mediaretriever.ContentResolverMediaRetriever;
//import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
//import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;
//
//import javax.inject.Singleton;
//
//import dagger.Binds;
//import dagger.Module;
//import dagger.Provides;
//
//@Module
//public class ContentResolverMediaRetrieverModule extends MediaRetrieverModule {
//
//    public ContentResolverMediaRetrieverModule(Context context) {
//        super(context);
//    }
//
//    @Override
//    public MediaRetriever provideMediaRetriever() {
//        return new ContentResolverMediaRetriever(context);
//    }
//
//}
