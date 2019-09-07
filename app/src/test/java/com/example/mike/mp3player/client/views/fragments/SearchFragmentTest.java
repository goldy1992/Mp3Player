package com.example.mike.mp3player.client.views.fragments;

import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchFragmentTest extends FragmentTestBase<SearchFragment> {


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(SearchFragment.class, false);
        super.addFragmentToActivity();
    }

    @Test
    public void testOnClickOnLayout() {
        FragmentScenario.FragmentAction<SearchFragment> action = this::clickOnLayout;
        performAction(action);
    }

    private void clickOnLayout(SearchFragment fragment) {
        fragment.onClickOnLayout(mock(View.class));
    }
}