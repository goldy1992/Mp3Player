package com.github.goldy1992.mp3player.service.library.data.search

import androidx.room.Entity

@Entity(tableName = "albums")
class Album(id: String?, value: String) : SearchEntity(id!!, value)