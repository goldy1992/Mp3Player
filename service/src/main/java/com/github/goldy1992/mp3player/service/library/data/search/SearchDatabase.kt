package com.github.goldy1992.mp3player.service.library.data.search

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Folder::class, Album::class], version = 3)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun folderDao(): FolderDao
    abstract fun albumDao(): AlbumDao

    companion object {
        private const val DATABASE_NAME = "normalised_search_db"
        private lateinit var instance: SearchDatabase
        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): SearchDatabase {
            instance = Room.databaseBuilder(context.applicationContext,
                    SearchDatabase::class.java, DATABASE_NAME
            )
                    .fallbackToDestructiveMigration()
                    .build()
            return instance
        }
    }
}