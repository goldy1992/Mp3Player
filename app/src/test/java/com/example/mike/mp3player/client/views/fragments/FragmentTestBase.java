package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity;

import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.core.util.Preconditions.checkState;

public class FragmentTestBase<F extends Fragment> {

    private static final String FRAGMENT_TAG = "FragmentScenario_Fragment_Tag";
    protected Context context;
    protected FragmentScenario<F> fragmentScenario;
    protected ActivityController<EmptyMediaActivityCompatFragmentActivity> activityScenario;
    Class<F> fragmentClass;

    protected void setup(Class<F> fragmentClass) {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.fragmentClass = fragmentClass;
        MockitoAnnotations.initMocks(this);
        //  Robolectric.buildActivity(EmptyMediaActivityCompatFragmentActivity.class).get();
        activityScenario =
        Robolectric.buildActivity(EmptyMediaActivityCompatFragmentActivity.class).setup();
        EmptyMediaActivityCompatFragmentActivity activity = activityScenario.get();

        Bundle fragmentArgs = new Bundle();
            Fragment fragment = activity.getSupportFragmentManager()
                    .getFragmentFactory().instantiate(
                            Preconditions.checkNotNull(fragmentClass.getClassLoader()),
                            fragmentClass.getName());
            fragment.setArguments(fragmentArgs);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(0, fragment, FRAGMENT_TAG)
                    .commitNow();

        this.context = InstrumentationRegistry.getInstrumentation().getContext();

    }

     void performAction(FragmentScenario.FragmentAction<F> action) {
         Fragment fragment = activityScenario.get().getSupportFragmentManager().findFragmentByTag(
                FRAGMENT_TAG);
         checkNotNull(fragment,
                 "The fragment has been removed from FragmentManager already.");
         checkState(fragmentClass.isInstance(fragment));
         action.perform(Preconditions.checkNotNull(fragmentClass.cast(fragment)));

    }

}
