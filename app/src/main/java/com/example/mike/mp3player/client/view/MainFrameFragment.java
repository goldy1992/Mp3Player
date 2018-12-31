package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MyDrawerListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class MainFrameFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private TitleBarFragment titleBarFragment;
    private MyRecyclerView recyclerView;
    private PlayToolBarFragment playToolBarFragment;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main_frame, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.drawerLayout = view.findViewById(R.id.drawer_layout);
        this.playToolBarFragment = (PlayToolBarFragment) getChildFragmentManager().findFragmentById(R.id.playToolbarFragment);
        this.titleBarFragment = (TitleBarFragment) getChildFragmentManager().findFragmentById(R.id.titleBarFragment);
        MyDrawerListener myDrawerListener = new MyDrawerListener();
        drawerLayout.addDrawerListener(myDrawerListener);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((MenuItem menuItem) -> onNavigationItemSelected(menuItem));

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(titleBarFragment.getTitleToolbar());
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs, MediaPlayerActionListener mediaPlayerActionListener) {
        this.recyclerView = getView().findViewById(R.id.myRecyclerView);
        this.getRecyclerView().initRecyclerView(songs, mediaPlayerActionListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public PlayToolBarFragment getPlayToolBarFragment() {
        return playToolBarFragment;
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        drawerLayout.closeDrawers();

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true;
    }


    public TitleBarFragment getTitleBarFragment() {
        return titleBarFragment;
    }

    public MyRecyclerView getRecyclerView() {
        return recyclerView;
    }
}
