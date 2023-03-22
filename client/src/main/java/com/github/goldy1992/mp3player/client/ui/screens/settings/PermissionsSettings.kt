@file:OptIn(
    ExperimentalMaterial3Api::class
)

package com.github.goldy1992.mp3player.client.ui.screens.settings

import android.Manifest
import android.content.Intent
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R

private const val logTag = "PemissionsSettings"

@Preview
@Composable
fun PermissionsMenuItems(permissionsProvider : () -> Map<String, Boolean> = {emptyMap<String, Boolean>()},
                                 requestPermission : (String) -> Unit = {_ ->}) {
    val context = LocalContext.current
    val permissions = permissionsProvider()
    Column {

        if (permissions.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val permissionDescription = stringResource(id = R.string.read_external_storage)
            PermissionListItem(
                permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                hasPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false,
                permissionDescription = permissionDescription,
                requestPermission = requestPermission) {
                Icon(
                    Icons.Default.Storage,
                    contentDescription = stringResource(id = R.string.read_external_storage)
                )
            }
        }
    }
}



@RequiresApi(TIRAMISU)
@Preview
@Composable
fun PermissionsMenuItemsTiramisu(permissionsProvider : () -> Map<String, Boolean> = { emptyMap() },
                                         requestPermission : (String) -> Unit = {_ ->}) {
    val permissions = permissionsProvider()
    Column {
        // Allow Notifications
        val hasNotificationPermissions = permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
        val notificationDescription = stringResource(id = R.string.allow_notifications)
        PermissionListItem(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            hasPermission = hasNotificationPermissions,
            permissionDescription = notificationDescription,
            requestPermission = requestPermission) {
            Icon(
                Icons.Default.Storage,
                contentDescription = notificationDescription
            )
        }


        // Allow Music Playback
        val hasMusicPlaybackPermissions = permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        val readMediaAudioDescription = stringResource(id = R.string.allow_audio_playback)
        PermissionListItem(
            permission = Manifest.permission.READ_MEDIA_AUDIO,
            hasPermission = hasMusicPlaybackPermissions,
            permissionDescription = readMediaAudioDescription,
            requestPermission = requestPermission) {
                Icon(
                    Icons.Default.MusicNote,
                    contentDescription = readMediaAudioDescription
                )
            }

        // Allow Album Art Display
        val hasImageDisplayPermissions = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val readMediaImagesDescription = stringResource(id = R.string.allow_image_display)
        PermissionListItem(
            permission = Manifest.permission.READ_MEDIA_IMAGES,
            hasPermission = hasImageDisplayPermissions,
            permissionDescription = readMediaImagesDescription,
            requestPermission = requestPermission) {
            Icon(
                Icons.Default.PhotoLibrary,
                contentDescription = readMediaImagesDescription
            )
        }
    }
}

@Composable
private fun PermissionListItem(
    permission: String,
    hasPermission: Boolean,
    permissionDescription: String,
    requestPermission: (String) -> Unit,
    icon: @Composable () -> Unit = {}
) {
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = icon,
        headlineText = { Text(text = permissionDescription) },
        trailingContent = {
            Switch(
                enabled = false,
                checked = hasPermission,
                colors = SwitchDefaults.colors(),
                modifier = Modifier.semantics { contentDescription = permissionDescription },
                onCheckedChange = {
                    Log.i(logTag, "on checked ${permission}")
                    requestPermission(permission)
                }
            )
        }
    )
}