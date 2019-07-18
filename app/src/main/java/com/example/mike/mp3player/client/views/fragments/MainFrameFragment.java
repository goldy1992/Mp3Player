package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MyDrawerListener;
import com.example.mike.mp3player.client.views.ThemeSpinnerController;
import com.example.mike.mp3player.client.views.fragments.viewpager.ViewPagerFragment;
import com.google.android.material.navigation.NavigationView;

public class MainFrameFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private TitleBarFragment titleBarFragment;
    private PlayToolBarFragment playToolBarFragment;
    private ViewPagerFragment viewPagerFragment;
    private NavigationView navigationView;
    private boolean enabled = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main_frame, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.drawerLayout = view.findViewById(R.id.drawer_layout);
        this.playToolBarFragment = (PlayToolBarFragment) getChildFragmentManager().findFragmentById(R.id.playToolbarFragment);
        this.viewPagerFragment = (ViewPagerFragment) getChildFragmentManager().findFragmentById(R.id.viewPagerFragment);
        this.titleBarFragment = (TitleBarFragment) getChildFragmentManager().findFragmentById(R.id.titleBarFragment);
        this.navigationView = view.findViewById(R.id.nav_view);
        initNavigationView();

        MyDrawerListener myDrawerListener = new MyDrawerListener();
        getDrawerLayout().addDrawerListener(myDrawerListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (enabled) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    getDrawerLayout().openDrawer(GravityCompat.START);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    public PlayToolBarFragment getPlayToolBarFragment() {
        return playToolBarFragment;
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        getDrawerLayout().closeDrawers();

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true;
    }

    private void initNavigationView() {
        getNavigationView().setNavigationItemSelectedListener((MenuItem menuItem) -> onNavigationItemSelected(menuItem));

        Spinner spinner = (Spinner) getNavigationView().getMenu().findItem(R.id.themes_menu_item).getActionView();

        ThemeSpinnerController themeSpinnerController = new ThemeSpinnerController(getContext(), spinner, getActivity());
    }


    public void enable() {
        this.enabled = true;
        getViewPagerFragment().enable();
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disable() {
        getDrawerLayout().closeDrawers();
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getViewPagerFragment().disable();
        this.enabled = false;

    }

    public boolean isEnabled() {
        return enabled;
    }

    public ViewPagerFragment getViewPagerFragment() {
        return viewPagerFragment;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
