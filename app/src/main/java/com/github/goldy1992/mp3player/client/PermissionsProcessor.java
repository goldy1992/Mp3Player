package com.github.goldy1992.mp3player.client;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

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

    private PermissionGranted permissionGranted;
    private Activity parentActivity;
    private static final String LOG_TAG = "PERMISSIONS_PROCESSOR";

    public PermissionsProcessor(Activity parentActivity, PermissionGranted permissionGranted) {
        this.parentActivity = parentActivity;
        this.permissionGranted = permissionGranted;
    }

    public void requestPermission(String permission)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(parentActivity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(parentActivity,
                        new String[]{permission},PERMISSION_RQ_CODE_MAP.get(permission));

        } else {
            // Permission has already been granted
            Log.i(LOG_TAG, "Permission has already been granted");
            permissionGranted.onPermissionGranted();
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
