package com.github.goldy1992.mp3player.client.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.goldy1992.mp3player.client.activities.TestMainActivity;

import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.core.util.Preconditions.checkState;

public class FragmentTestBase<F extends Fragment> {

    private static final String FRAGMENT_TAG = "FragmentScenario_Fragment_Tag";
    protected Context context;
    protected Fragment fragment;
    protected ActivityController<TestMainActivity> activityScenario;
    private TestMainActivity activity;
    Class<F> fragmentClass;

    protected void setup(Class<F> fragmentClass, boolean addFragmentToActivity) {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.fragmentClass = fragmentClass;
        MockitoAnnotations.initMocks(this);
        activityScenario =
                Robolectric.buildActivity(TestMainActivity.class).setup();
        this.activity = activityScenario.get();

        Bundle fragmentArgs = new Bundle();
        this.fragment = activity.getSupportFragmentManager()
                .getFragmentFactory().instantiate(
                        Preconditions.checkNotNull(fragmentClass.getClassLoader()),
                        fragmentClass.getName());
        fragment.setArguments(fragmentArgs);
        if (addFragmentToActivity) {
            addFragmentToActivity();
        }
    }

    protected void setup(Fragment fragment, Class<F> fragmentClass) {
        this.fragmentClass = fragmentClass;
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.fragment = fragment;
        MockitoAnnotations.initMocks(this);
        activityScenario =
                Robolectric.buildActivity(TestMainActivity.class).setup();
        this.activity = activityScenario.get();
        addFragmentToActivity();

    }

    public void addFragmentToActivity() {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(0, fragment, FRAGMENT_TAG)
                .commitNow();
    }

    protected void performAction(FragmentScenario.FragmentAction<F> action) {
        Fragment fragment = activityScenario.get().getSupportFragmentManager().findFragmentByTag(
                FRAGMENT_TAG);
        checkNotNull(fragment,
                "The fragment has been removed from FragmentManager already.");
        checkState(fragmentClass.isInstance(fragment));
        action.perform(Preconditions.checkNotNull(fragmentClass.cast(fragment)));

    }

}
