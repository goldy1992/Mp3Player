package com.example.mike.mp3player.client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.PermissionsProcessor;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 0;

    private PermissionsProcessor permissionsProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionsProcessor= new PermissionsProcessor(this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
    }

    public void init() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(), MediaPlayerActivity.class);

            }
        });
    }

    public void buildMediaLibrary() {
//        ArrayList<MediaBrowserCompat.MediaItem> arrayList = new ArrayList<>();
//        ListView listView = (ListView) findViewById(R.id.list);
//        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
//        // and the array that contains the data
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
//
//        // Here, you set the data in your ListView
//        listView.setAdapter(adapter);
//
//            MediaLibrary mediaLibrary = new MediaLibrary();
//            mediaLibrary.init();
//            mediaLibrary.buildMediaLibrary();
//
//            for (File f : mediaLibrary.getLibrary().keySet()) {
//                for (MediaBrowserCompat.MediaItem track : mediaLibrary.getLibrary().get(f)) {
//                    arrayList.add(track.getName());
//                }
//            }
//            adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int READ_REQUEST_CODE = 42;

    public void sendMessage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                intent.putExtra("uri", uri);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        }
    }
}

