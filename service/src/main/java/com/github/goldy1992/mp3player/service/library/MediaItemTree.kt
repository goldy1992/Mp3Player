package com.github.goldy1992.mp3player.service.library

import android.content.res.AssetManager
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.*
import androidx.media3.common.util.Util
import com.google.common.collect.ImmutableList
import org.json.JSONObject

/**
 * A sample media catalog that represents media items as a tree.
 *
 * It fetched the data from {@code catalog.json}. The root's children are folders containing media
 * items from the same album/artist/genre.
 *
 * Each app should have their own way of representing the tree. MediaItemTree is used for
 * demonstration purpose only.
 */
object MediaItemTree {
    private var treeNodes: MutableMap<String, MediaItemNode> = mutableMapOf()
    private var titleMap: MutableMap<String, MediaItemNode> = mutableMapOf()
    private var isInitialized = false
    private const val ROOT_ID = "[rootID]"
    private const val ALBUM_ID = "[albumID]"
    private const val GENRE_ID = "[genreID]"
    private const val ARTIST_ID = "[artistID]"
    private const val ALBUM_PREFIX = "[album]"
    private const val GENRE_PREFIX = "[genre]"
    private const val ARTIST_PREFIX = "[artist]"
    private const val ITEM_PREFIX = "[item]"

    private class MediaItemNode(val item: MediaItem) {
        private val children: MutableList<MediaItem> = ArrayList()

        fun addChild(childID: String) {
            this.children.add(treeNodes[childID]!!.item)
        }

        fun getChildren(): List<MediaItem> {
            return ImmutableList.copyOf(children)
        }
    }

    private fun buildMediaItem(
        title: String,
        mediaId: String,
        isPlayable: Boolean,
        @MediaMetadata.FolderType folderType: Int,
        album: String? = null,
        artist: String? = null,
        genre: String? = null,
        sourceUri: Uri? = null,
        imageUri: Uri? = null,
    ): MediaItem {
        // TODO(b/194280027): add artwork
        val metadata =
            MediaMetadata.Builder()
                .setAlbumTitle(album)
                .setTitle(title)
                .setArtist(artist)
                .setGenre(genre)
                .setFolderType(folderType)
                .setIsPlayable(isPlayable)
                .setArtworkUri(imageUri)
                .build()
        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setMediaMetadata(metadata)
            .setUri(sourceUri)
            .build()
    }

    private fun loadJSONFromAsset(assets: AssetManager): String {
        val buffer = assets.open("catalog.json").use { Util.toByteArray(it) }
        return String(buffer, Charsets.UTF_8)
    }

    fun initialize(assets: AssetManager) {
        if (isInitialized) return
        isInitialized = true
        // create root and folders for album/artist/genre.
        treeNodes[ROOT_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Root Folder",
                    mediaId = ROOT_ID,
                    isPlayable = false,
                    folderType = FOLDER_TYPE_MIXED
                )
            )
        treeNodes[ALBUM_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Album Folder",
                    mediaId = ALBUM_ID,
                    isPlayable = false,
                    folderType = FOLDER_TYPE_MIXED
                )
            )
        treeNodes[ARTIST_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Artist Folder",
                    mediaId = ARTIST_ID,
                    isPlayable = false,
                    folderType = FOLDER_TYPE_MIXED
                )
            )
        treeNodes[GENRE_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Genre Folder",
                    mediaId = GENRE_ID,
                    isPlayable = false,
                    folderType = FOLDER_TYPE_MIXED
                )
            )
        treeNodes[ROOT_ID]!!.addChild(ALBUM_ID)
        treeNodes[ROOT_ID]!!.addChild(ARTIST_ID)
        treeNodes[ROOT_ID]!!.addChild(GENRE_ID)

        // Here, parse the json file in asset for media list.
        // We use a file in asset for demo purpose
        val jsonObject = JSONObject(loadJSONFromAsset(assets))
        val mediaList = jsonObject.getJSONArray("media")

        // create subfolder with same artist, album, etc.
        for (i in 0 until mediaList.length()) {
            addNodeToTree(mediaList.getJSONObject(i))
        }
    }

    private fun addNodeToTree(mediaObject: JSONObject) {

        val id = mediaObject.getString("id")
        val album = mediaObject.getString("album")
        val title = mediaObject.getString("title")
        val artist = mediaObject.getString("artist")
        val genre = mediaObject.getString("genre")
        val sourceUri = Uri.parse(mediaObject.getString("source"))
        val imageUri = Uri.parse(mediaObject.getString("image"))
        // key of such items in tree
        val idInTree = ITEM_PREFIX + id
        val albumFolderIdInTree = ALBUM_PREFIX + album
        val artistFolderIdInTree = ARTIST_PREFIX + artist
        val genreFolderIdInTree = GENRE_PREFIX + genre

        treeNodes[idInTree] =
            MediaItemNode(
                buildMediaItem(
                    title = title,
                    mediaId = idInTree,
                    isPlayable = true,
                    album = album,
                    artist = artist,
                    genre = genre,
                    sourceUri = sourceUri,
                    imageUri = imageUri,
                    folderType = FOLDER_TYPE_NONE
                )
            )

        titleMap[title.lowercase()] = treeNodes[idInTree]!!

        if (!treeNodes.containsKey(albumFolderIdInTree)) {
            treeNodes[albumFolderIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = album,
                        mediaId = albumFolderIdInTree,
                        isPlayable = true,
                        folderType = FOLDER_TYPE_PLAYLISTS
                    )
                )
            treeNodes[ALBUM_ID]!!.addChild(albumFolderIdInTree)
        }
        treeNodes[albumFolderIdInTree]!!.addChild(idInTree)

        // add into artist folder
        if (!treeNodes.containsKey(artistFolderIdInTree)) {
            treeNodes[artistFolderIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = artist,
                        mediaId = artistFolderIdInTree,
                        isPlayable = true,
                        folderType = FOLDER_TYPE_PLAYLISTS
                    )
                )
            treeNodes[ARTIST_ID]!!.addChild(artistFolderIdInTree)
        }
        treeNodes[artistFolderIdInTree]!!.addChild(idInTree)

        // add into genre folder
        if (!treeNodes.containsKey(genreFolderIdInTree)) {
            treeNodes[genreFolderIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = genre,
                        mediaId = genreFolderIdInTree,
                        isPlayable = true,
                        folderType = FOLDER_TYPE_PLAYLISTS
                    )
                )
            treeNodes[GENRE_ID]!!.addChild(genreFolderIdInTree)
        }
        treeNodes[genreFolderIdInTree]!!.addChild(idInTree)
    }

    fun getItem(id: String): MediaItem? {
        return treeNodes[id]?.item
    }

    fun getRootItem(): MediaItem {
        return treeNodes[ROOT_ID]!!.item
    }

    fun getChildren(id: String): List<MediaItem>? {
        return treeNodes[id]?.getChildren()
    }

    fun getRandomItem(): MediaItem {
        var curRoot = getRootItem()
        while (curRoot.mediaMetadata.folderType != FOLDER_TYPE_NONE) {
            val children = getChildren(curRoot.mediaId)!!
            curRoot = children.random()
        }
        return curRoot
    }

    fun getItemFromTitle(title: String): MediaItem? {
        return titleMap[title]?.item
    }
}