package com.github.goldy1992.mp3player.service.library.search

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Folder::class], version = 2)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun folderDao(): FolderDao

    companion object {
        private const val DATABASE_NAME = "normalised_search_db"
        private lateinit var instance: SearchDatabase
        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): SearchDatabase {
            instance = Room.databaseBuilder(context.applicationContext,
                    SearchDatabase::class.java, DATABASE_NAME)
                    .build()
            return instance
        }
    }
}