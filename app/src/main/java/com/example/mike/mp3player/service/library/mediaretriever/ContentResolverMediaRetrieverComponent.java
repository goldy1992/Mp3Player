package com.example.mike.mp3player.service.library.mediaretriever;

import com.example.mike.mp3player.service.library.ContentResolverMediaRetrieverModule;

import dagger.Component;

@Component(modules = ContentResolverMediaRetrieverModule.class)
public interface ContentResolverMediaRetrieverComponent extends MediaRetrieverComponent {
}
