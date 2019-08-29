package com.example.mike.mp3player.client.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.LogTagger;
import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.search.SearchResultListener;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.SearchAdapterTouchListener;
import com.example.mike.mp3player.client.views.SearchResultAdapter;

import java.util.List;

public abstract class SearchResultActivity extends MediaActivityCompat implements SearchResultListener, LogTagger,
    SearchAdapterTouchListener.ItemSelectedListener {
    private String currentQuery;
    private RecyclerView recyclerView;
    private SearchResultAdapter searchResultAdapter;
    private SearchView searchView;
    private Toolbar toolbar;

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NONE;
    }

    @Override
    String getWorkerId() {
        return "SRCH_RSLT_ACTVTY";
    }

    @Override
    public void onConnected() {
        super.onConnected();
        initialiseView(R.layout.activity_search_results);
        this.getMediaBrowserAdapter().registerSearchResultListener(this);
        handleIntent(getIntent());
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        this.toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.searchResultAdapter = new SearchResultAdapter(getApplicationContext());
        this.searchView = findViewById(R.id.search_view);
        this.recyclerView = findViewById(R.id.result_recycle_view);
        this.recyclerView.setAdapter(searchResultAdapter);
        this.recyclerView.addOnItemTouchListener(new SearchAdapterTouchListener(getApplicationContext(), this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), SearchResultActivityInjector.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        //    Assumes current activity is the searchable activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setBackgroundColor(Color.WHITE);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void itemSelected(MediaItem item) {

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.currentQuery = intent.getStringExtra(SearchManager.QUERY);
            if (null != searchView) {
                this.searchView.setQuery(currentQuery, false);
            }
            getMediaBrowserAdapter().search(currentQuery, null);
//            showResults(query);
        }
    }

    private void showResults(String query) {
        // Query your data set and show results
        // ...
    }

    @Override
    public void onSearchResult(List<MediaItem> searchResults) {
        searchResultAdapter.items.addAll(searchResults);
        searchResultAdapter.notifyDataSetChanged();
        Log.i(getLogTag(), "received search results");
    }

    @Override
    public String getLogTag() {
        return "SRCH_RSLT_ACTVTY";
    }
}
