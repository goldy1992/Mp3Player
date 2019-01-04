package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClickableSurfaceFragment<V extends View> extends Fragment {

    private V view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_clickable_surface_view, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.view = view.findViewById(R.id.view);
    }

    public void setView(@LayoutRes int layoutRes) {
        getLayoutInflater().inflate(layoutRes, this.view);
    }
}
