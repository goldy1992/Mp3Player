package com.example.mike.mp3player.shadows;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import org.mockito.internal.util.reflection.Fields;
import org.mockito.internal.util.reflection.InstanceField;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import static org.robolectric.shadow.api.Shadow.directlyOn;

@Implements(FragmentPagerAdapter.class)
public class ShadowFragmentPagerAdapter {

    @RealObject
    FragmentPagerAdapter realObject;

    @Implementation
    public void finishUpdate(@NonNull ViewGroup container) {
        FragmentManager fragmentManager = getFragmentManagerFromAdapter(realObject);
        if (fragmentManager.getFragments().isEmpty()) {
            directlyOn(realObject, PagerAdapter.class).finishUpdate(container);
        }
    }

    private FragmentManager getFragmentManagerFromAdapter(PagerAdapter adapter) {
        for (InstanceField instanceField : Fields.allDeclaredFieldsOf(adapter).instanceFields()) {
            Object obj = instanceField.read();
            if (obj instanceof FragmentManager) {
                return (FragmentManager) obj;
            }
        }
        return null;
    }

}