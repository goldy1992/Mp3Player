package com.github.goldy1992.mp3player.service.library.data.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
class Album(
    @PrimaryKey val id: String,
    val value: String)