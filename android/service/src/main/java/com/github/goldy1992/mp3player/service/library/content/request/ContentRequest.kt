package com.github.goldy1992.mp3player.service.library.content.request

import androidx.annotation.VisibleForTesting

class ContentRequest @VisibleForTesting constructor(val queryString: String,
                                                    val contentRetrieverKey: String,
                                                    val mediaIdPrefix: String?)