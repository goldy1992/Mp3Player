package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity;

import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.core.util.Preconditions.checkState;

public class FragmentTestBase<F extends Fragment> {

    private static final String FRAGMENT_TAG = "FragmentScenario_Fragment_Tag";
    protected Context context;
    protected FragmentScenario<F> fragmentScenario;
    protected Fragment fragment;
    protected ActivityController<EmptyMediaActivityCompatFragmentActivity> activityScenario;
    private EmptyMediaActivityCompatFragmentActivity activity;
    Class<F> fragmentClass;

    protected void setup(Class<F> fragmentClass, boolean addFragmentToActivity) {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.fragmentClass = fragmentClass;
        MockitoAnnotations.initMocks(this);
        activityScenario =
                Robolectric.buildActivity(EmptyMediaActivityCompatFragmentActivity.class).setup();
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
