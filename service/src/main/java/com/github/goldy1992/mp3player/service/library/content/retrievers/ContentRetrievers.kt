package com.github.goldy1992.mp3player.service.library.content.retrievers

import com.github.goldy1992.mp3player.commons.MediaItemType

interface ContentRetrievers {

    fun getContentRetriever(mediaItemType: MediaItemType) : ContentRetriever?
    fun songsRetriever() : ContentResolverRetriever
    fun songsFromFolderRetriever() : ContentResolverRetriever
    fun songsFromAlbumRetriever() : ContentResolverRetriever
    fun foldersRetriever() : ContentResolverRetriever
    fun albumsRetriever() : ContentResolverRetriever
    fun rootRetriever() : RootRetriever
}