package com.github.goldy1992.mp3player.service.library.search

import androidx.room.PrimaryKey

abstract class SearchEntity(@field:PrimaryKey val id: String, val value: String)