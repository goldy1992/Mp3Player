package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;

public class FragmentTestBase<F extends Fragment> {

    protected Context context;
    protected FragmentScenario<F> fragmentScenario;

    protected void setup(Class<F> clazz) {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        FragmentFactory fragmentFactory = new FragmentFactory();
        Bundle args = new Bundle();
        this.fragmentScenario = FragmentScenario.launch(clazz, args, R.style.AppTheme_Orange, fragmentFactory);
    }
}
