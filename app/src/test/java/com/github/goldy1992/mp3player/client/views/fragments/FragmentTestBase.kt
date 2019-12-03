package com.github.goldy1992.mp3player.client.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.activities.TestMainActivity
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

open class FragmentTestBase<F : Fragment> {


    protected var context: Context? = null
    protected var fragment: Fragment? = null
    protected var activityScenario: ActivityController<TestMainActivity>? = null
    private var activity: TestMainActivity? = null
    var fragmentClass: Class<out F>? = null

    protected fun setup(fragmentClass: Class<F>, addFragmentToActivity: Boolean) {
        context = InstrumentationRegistry.getInstrumentation().context
        this.fragmentClass = fragmentClass
        MockitoAnnotations.initMocks(this)
        activityScenario = Robolectric.buildActivity(TestMainActivity::class.java).setup()
        activity = activityScenario!!.get()
        val fragmentArgs = Bundle()
        fragment = activity!!.supportFragmentManager
                .fragmentFactory.instantiate(
                Preconditions.checkNotNull(fragmentClass.classLoader),
                fragmentClass.name)
        fragment!!.arguments = fragmentArgs
        if (addFragmentToActivity) {
            addFragmentToActivity()
        }
    }

    protected fun setup(fragment: Fragment?, fragmentClass: Class<F>) {
        this.fragmentClass = fragmentClass
        context = InstrumentationRegistry.getInstrumentation().context
        this.fragment = fragment
        MockitoAnnotations.initMocks(this)
        activityScenario = Robolectric.buildActivity(TestMainActivity::class.java).setup()
        activity = activityScenario!!.get()
        addFragmentToActivity()
    }

    fun addFragmentToActivity() {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(0, fragment!!, FRAGMENT_TAG)
                .commitNow()
    }

    protected fun performAction(action: FragmentAction<F>) {
        val fragment = activityScenario!!.get().supportFragmentManager.findFragmentByTag(
                FRAGMENT_TAG)
        Preconditions.checkNotNull(fragment,
                "The fragment has been removed from FragmentManager already.")
        Preconditions.checkState(fragmentClass!!.isInstance(fragment))
        action.perform(Preconditions.checkNotNull(fragmentClass!!.cast(fragment)))
    }

    companion object {
        private const val FRAGMENT_TAG = "FragmentScenario_Fragment_Tag"
    }
}