package com.github.goldy1992.mp3player.client.views.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.R
import kotlinx.android.synthetic.main.fragment_simple_title_bar.*

class SimpleTitleBarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_title_bar, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {

        val myActivity : AppCompatActivity? = requireActivity() as AppCompatActivity
        myActivity!!.setSupportActionBar(titleToolbar)
        myActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val typedValue = TypedValue()
        val theme = requireContext().theme
//        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data
        titleToolbar.navigationIcon!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

}