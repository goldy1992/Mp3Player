package com.github.goldy1992.mp3player.service.library.search

import androidx.room.Entity

@Entity(tableName = "folders")
class Folder(id: String?, value: String) : SearchEntity(id!!, value)