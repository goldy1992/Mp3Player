package com.github.goldy1992.mp3player.service.library.data.search

import androidx.room.Entity

@Entity(tableName = "songs")
class Song(id: String?, value: String) : SearchEntity(id!!, value)