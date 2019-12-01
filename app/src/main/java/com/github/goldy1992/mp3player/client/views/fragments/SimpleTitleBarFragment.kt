package com.github.goldy1992.mp3player.client.views.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.R

class SimpleTitleBarFragment : Fragment() {
    var toolbar: Toolbar? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_title_bar, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        toolbar = view.findViewById(R.id.titleToolbar)
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data
        toolbar.getNavigationIcon()!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

}