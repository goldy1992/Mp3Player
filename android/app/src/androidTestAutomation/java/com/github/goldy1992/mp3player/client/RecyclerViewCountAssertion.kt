package com.github.goldy1992.mp3player.client

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.Assert.assertEquals

class RecyclerViewCountAssertion(val expectedCount : Int) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        val recyclerView : RecyclerView = view as RecyclerView
        val adapter : RecyclerView.Adapter<RecyclerView.ViewHolder> = recyclerView.adapter as
                RecyclerView.Adapter<RecyclerView.ViewHolder>
        assertEquals(expectedCount, adapter.itemCount)
    }


}