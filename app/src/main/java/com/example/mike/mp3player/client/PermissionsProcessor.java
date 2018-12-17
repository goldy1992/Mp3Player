package com.example.mike.mp3player.client;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PermissionsProcessor {

    private static final Map<String, Integer> PERMISSION_RQ_CODE_MAP = new HashMap<>();

    {
        PERMISSION_RQ_CODE_MAP.put(WRITE_EXTERNAL_STORAGE, 0);
    }

    Activity parentActivity;

    public PermissionsProcessor(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void requestPermission(String permission)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(parentActivity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(parentActivity, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission

                ActivityCompat.requestPermissions(parentActivity,
                        new String[]{permission},PERMISSION_RQ_CODE_MAP.get(permission));
            }
        } else {
            // Permission has already been granted
            ((MainActivity) parentActivity).init();
        }
    }

    public String getPermissionFromRequestCode(int requestCode) {
        for (String permission : PERMISSION_RQ_CODE_MAP.keySet()) {
            if (PERMISSION_RQ_CODE_MAP.get(permission) == requestCode) {
                return permission;
            }
        }
        return null;
    }
}
