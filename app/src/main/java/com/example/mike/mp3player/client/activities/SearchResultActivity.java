package com.example.mike.mp3player.client.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.mike.mp3player.LogTagger;
import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.search.SearchResultListener;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import java.util.List;

public abstract class SearchResultActivity extends MediaActivityCompat implements SearchResultListener, LogTagger {

    private String currentQuery;
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
        this.toolbar = findViewById(R.id.titleToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.currentQuery = intent.getStringExtra(SearchManager.QUERY);
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
        Log.i(getLogTag(), "received search results");
    }

    @Override
    public String getLogTag() {
        return "SRCH_RSLT_ACTVTY";
    }
}