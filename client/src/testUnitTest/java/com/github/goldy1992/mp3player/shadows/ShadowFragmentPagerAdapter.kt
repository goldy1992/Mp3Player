package com.github.goldy1992.mp3player.shadows

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import org.mockito.internal.util.reflection.Fields
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadow.api.Shadow

@Implements(FragmentPagerAdapter::class)
class ShadowFragmentPagerAdapter {

    @RealObject
    var realObject: FragmentPagerAdapter? = null

    @Implementation
    fun finishUpdate(container: ViewGroup) {
        val fragmentManager = getFragmentManagerFromAdapter(realObject)
        if (fragmentManager!!.fragments.isEmpty()) {
            Shadow.directlyOn(realObject, PagerAdapter::class.java).finishUpdate(container)
        }
    }

    private fun getFragmentManagerFromAdapter(adapter: PagerAdapter?): FragmentManager? {
        for (instanceField in Fields.allDeclaredFieldsOf(adapter).instanceFields()) {
            val obj = instanceField.read()
            if (obj is FragmentManager) {
                return obj
            }
        }
        return null
    }
}