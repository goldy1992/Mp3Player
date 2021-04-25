package com.github.goldy1992.mp3player.client

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.github.goldy1992.mp3player.client.activities.HiltTestActivity

class FragmentHiltScenario<F :  Fragment>

    constructor(
        private val scenario : ActivityScenario<HiltTestActivity>,
        private val fragmentTag: String? = "fragmentTag") {
    companion object {
        /**
         * launchFragmentInContainer from the androidx.fragment:fragment-testing library
         * is NOT possible to use right now as it uses a hardcoded Activity under the hood
         * (i.e. [EmptyFragmentActivity]) which is not annotated with @AndroidEntryPoint.
         *
         * As a workaround, use this function that is equivalent. It requires you to add
         * [HiltTestActivity] in the debug folder and include it in the debug AndroidManifest.xml file
         * as can be found in this project.
         */
        inline fun <reified T : Fragment> launch3FragmentInHiltContainer(
                fragmentArgs: Bundle? = null,
                @StyleRes themeResId: Int = R.style.AppTheme,
                navHostController: NavHostController? = null,
                fragmentFactory: FragmentFactory? = null,
                fragmentTag: String? = "FragmentTag",

                ): FragmentHiltScenario<T> {
            val startActivityIntent = Intent.makeMainActivity(
                    ComponentName(
                            ApplicationProvider.getApplicationContext(),
                            HiltTestActivity::class.java
                    )
            ).putExtra(EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId)

            val scenario = ActivityScenario.launch<HiltTestActivity>(startActivityIntent)
            scenario.onActivity { activity ->
                fragmentFactory?.let {
                    activity.supportFragmentManager.fragmentFactory = it
                }
                val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                        Preconditions.checkNotNull(T::class.java.classLoader),
                        T::class.java.name
                )
                fragment.arguments = fragmentArgs
                navHostController?.let {
                    fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                        if (viewLifecycleOwner != null) {
                            Navigation.setViewNavController(fragment.requireView(), it)
                        }
                    }
                }
                activity.supportFragmentManager
                        .beginTransaction()
                        .add(android.R.id.content, fragment, fragmentTag)
                        .commitNow()


            }
            return FragmentHiltScenario<T>(scenario, fragmentTag)
        }

    }

    fun onFragment(action: Fragment.() -> Unit = {}) {
        this.scenario.onActivity {
            val f : F? = it.supportFragmentManager.findFragmentByTag(fragmentTag) as? F
            f?.action()
        }
    }
}


