package com.example.mike.mp3player.client.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MainActivityInjector;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mike.mp3player.commons.Constants.THEME;

public class ThemeSpinnerController implements AdapterView.OnItemSelectedListener {

    private static final int[] attrs = {R.attr.themeName};
    private static final String LOG_TAG = "THM_SPNR_CTLR";
    private final Activity activity;
    private Context context;
    private Spinner spinner;
    private ArrayAdapter<String> adapter; // TODO: make a make from Theme name to resource
    private List<Integer> themeResIds;
    private BiMap<String, Integer> themeNameToResMap;
    private long selectCount = 0;
    private String currentTheme;

    public ThemeSpinnerController(@NonNull Context context, @NonNull Spinner spinner, @NonNull Activity activity) {
        this.context = context;
        this.spinner = spinner;
        this.activity = activity;
        init();
    }

    private void init() {
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        TypedArray themeArray = context.getResources().obtainTypedArray(R.array.themes);
        this.themeResIds = new ArrayList<>();
        this.themeNameToResMap = HashBiMap.create();

        if (themeArray != null && themeArray.length() > 0) {
            int numberOfResources = themeArray.length();
            for (int i = 0; i < numberOfResources; i++) { // for each theme in the theme array
                int res = themeArray.getResourceId(i, 0);
                themeResIds.add(res);
                TypedArray themeNameArray = context.obtainStyledAttributes(res, attrs); // get the theme name GIVEN the themes res if.
                String themeName = themeNameArray.getString(0);
                adapter.add(themeName);
                getThemeNameToResMap().put(themeName, res);
                recycleTypedArray(themeNameArray);
            }
        }

        recycleTypedArray(themeArray);
        int currentThemeId = getCurrentThemeId();
        if (currentThemeId != -1) {
            currentTheme = getThemeNameToResMap().inverse().get(currentThemeId);
            int position = adapter.getPosition(getCurrentTheme());
            spinner.setSelection(position);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int res = themeResIds.get(position);
        Log.d(LOG_TAG, "selected " + getThemeNameToResMap().get(res));
        if (selectCount >= 1) {
            Log.d(LOG_TAG, "select count > 1");
            setThemePreference(res);
            activity.finish();
            Intent intent = new Intent(context, MainActivityInjector.class);
            intent.putExtra(THEME, res);
            activity.startActivity(intent);
        }
        selectCount++;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     *
     * @return
     */
    private int getCurrentThemeId() {
        Resources.Theme activityTheme = activity.getTheme();
        if (null != activityTheme) {

            TypedArray themeNameArray = activityTheme.obtainStyledAttributes(attrs);
            if (null != themeNameArray && themeNameArray.length() > 0) {
                String themeName = themeNameArray.getString(0);
                themeNameArray.recycle();
                Log.d(LOG_TAG, "current theme is: " + themeName);
                Integer result = getThemeNameToResMap().get(themeName);
                return result == null ? -1 : result;
            }
        }
        return  -1;
    }
    /**
     *
     * @param themeId the theme id
     */
    private void setThemePreference(int themeId) {
        SharedPreferences settings = context.getSharedPreferences(THEME, MODE_PRIVATE);
        if (settings != null) {
            SharedPreferences.Editor editor = settings.edit();
            if (editor != null) {
                editor.putInt(THEME, themeId);
                editor.apply();
            }
        }
    }
    /**
     *
     */
    private void recycleTypedArray(TypedArray typedArray) {
        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @VisibleForTesting
    public BiMap<String, Integer> getThemeNameToResMap() {
        return themeNameToResMap;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }
}
