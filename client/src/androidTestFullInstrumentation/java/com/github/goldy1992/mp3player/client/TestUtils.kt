package com.github.goldy1992.mp3player.client

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals


object TestUtils {



    fun assertTabName(tabLayout: TabLayout, position: Int, expectedTabTitle: String?) {
        val tab = tabLayout.getTabAt(position)
        val actualNameFirstTab = tab!!.text.toString()
        assertEquals(expectedTabTitle, actualNameFirstTab)
    }

    fun <VH : RecyclerView.ViewHolder?> actionOnItemViewAtPosition(position: Int,
                                                                   @IdRes viewId: Int,
                                                                   viewAction: ViewAction): ViewAction? {
        return ActionOnItemViewAtPositionViewAction<VH>(position, viewId, viewAction)
    }

    private class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder?>(private val position: Int,
                                                                                      @param:IdRes private val viewId: Int,
                                                                                      private val viewAction: ViewAction) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                    ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed()
            )
        }

        override fun getDescription(): String {
            return ("actionOnItemAtPosition performing ViewAction: "
                    + viewAction.description
                    + " on item at position: "
                    + position)
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            ScrollToPositionViewAction(position).perform(uiController, view)
            uiController.loopMainThreadUntilIdle()
            val targetView: View = recyclerView.getChildAt(position).findViewById(viewId)
            if (targetView == null) {
                throw PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(
                                HumanReadables.describe(view))
                        .withCause(IllegalStateException(
                                "No view with id "
                                        + viewId
                                        + " found at position: "
                                        + position))
                        .build()
            } else {
                viewAction.perform(uiController, targetView)
            }
        }

    }

    private class ScrollToPositionViewAction(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed()
            )
        }

        override fun getDescription(): String {
            return "scroll RecyclerView to position: " + position
        }

        override fun perform(uiController: UiController?, view: View) {
            val recyclerView = view as RecyclerView
            recyclerView.scrollToPosition(position)
        }

    }


    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
        return RecyclerViewMatcher(recyclerViewId)
    }



}