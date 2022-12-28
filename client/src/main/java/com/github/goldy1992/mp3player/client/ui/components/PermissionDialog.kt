package com.github.goldy1992.mp3player.client.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissions(
    snackbarHostState: SnackbarHostState,
    scope : CoroutineScope
) {

    // Camera permission state
    val storagePermissionsState = rememberPermissionState(
        android.Manifest.permission.READ_MEDIA_AUDIO
    )

    if (!storagePermissionsState.status.isGranted) {
        Column {
            val textToShow = if (storagePermissionsState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The camera is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Column(Modifier.fillMaxSize()) {
                Log.i("permissions", "showing button")
                Button(onClick = {
                    //snackbarHostState.showSnackbar(textToShow)
                    storagePermissionsState.launchPermissionRequest() }) {
                    Text("click for permissions")
                }
            }
            LaunchedEffect(Unit) {
                scope.launch {  }
                Log.i("permissions", "loading tiramisu permissions")

            }
        }
    }
}