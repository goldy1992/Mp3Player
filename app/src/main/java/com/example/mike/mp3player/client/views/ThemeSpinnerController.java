package com.example.mike.mp3player.client.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.THEME;

public class ThemeSpinnerController implements AdapterView.OnItemSelectedListener {
    private final MediaActivityCompat activity;
    private Context context;
    private Spinner spinner;
    private ArrayAdapter<String> adapter; // TODO: make a make from Theme name to resource
    private List<Integer> themeResIds;
    private long selectCount = 0;

    public ThemeSpinnerController(@NonNull Context context, @NonNull Spinner spinner, @NonNull MediaActivityCompat activity) {
        this.context = context;
        this.spinner = spinner;
        this.activity = activity;
        init();
    }

    private void init() {
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.themes);
        this.themeResIds = new ArrayList<>();


        if (typedArray != null && typedArray.length() > 0) {
            int numberOfResources = typedArray.length();
            for (int i = 0; i < numberOfResources; i++) {
                int res = typedArray.getResourceId(i, 0);
                themeResIds.add(res);
                int[] attrs = {R.attr.themeName};
                TypedArray attrArray = context.obtainStyledAttributes(res, attrs);
                adapter.add(attrArray.getString(0));

            }

        }
        typedArray.recycle();
   //     adapter.addAll(themeResIds);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int res = themeResIds.get(position);

        if (selectCount >= 1) {
            activity.finish();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(THEME, res);
            activity.startActivity(intent);
        }
        selectCount++;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
