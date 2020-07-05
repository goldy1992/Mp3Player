package com.github.goldy1992.mp3player.actions

import android.content.Context
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.github.goldy1992.mp3player.client.AwaitingMediaControllerIdlingResource
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.callbacks.AndroidTestMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import org.hamcrest.Matcher


class RegisterIdlingResourceAction
    constructor
    (private val awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource)
    : ViewAction {
    /**
     * Returns a description of the view action. The description should not be overly long and should
     * fit nicely in a sentence like: "performing %description% action on view with id ..."
     */
    override fun getDescription(): String {
        return "registerIdlingResource"
    }

    /**
     * A mechanism for ViewActions to specify what type of views they can operate on.
     *
     *
     * A ViewAction can demand that the view passed to perform meets certain constraints. For
     * example it may want to ensure the view is already in the viewable physical screen of the device
     * or is of a certain type.
     *
     * @return a [
 * `Matcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html) that will be tested prior to calling perform.
     */
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(View::class.java)
   }

    /**
     * Performs this action on the given view.
     *
     * @param uiController the controller to use to interact with the UI.
     * @param view the view to act upon. never null.
     */
    override fun perform(uiController: UiController?, view: View?) {
        val context: Context = view!!.context
        if (context is MediaActivityCompat) {
            val mediaActivityCompat : MediaActivityCompat = context
            val myMediaControllerCallback : MyMediaControllerCallback = mediaActivityCompat.mediaControllerAdapter.myMediaControllerCallback
            if (myMediaControllerCallback is AndroidTestMediaControllerCallback)
            {
                myMediaControllerCallback.awaitingMediaControllerIdlingResource = this.awaitingMediaControllerIdlingResource
            }
        }
    }
}