package com.github.goldy1992.mp3player.shadows

import android.content.Context
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.nhaarman.mockitokotlin2.mock

import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(Glide::class)
class ShadowGlide {


    object Companion {
        @Implementation
        fun with(context: Context): RequestManager {
            return mock<RequestManager>()
        }

        @Implementation
        fun with(fragment: Fragment): RequestManager {
            return mock<RequestManager>()
        }
    }
}