package com.github.goldy1992.mp3player.client.ui.screens.settings

import android.Manifest
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R

private const val logTag = "PermissionsSettings"


fun permissionsMenuItemsPreTiramisu(
    lazyListScope: LazyListScope,
    permissionsProvider : () -> Map<String, Boolean> = {emptyMap()},
                        ) {
    val permissions = permissionsProvider()
    lazyListScope.item {
        if (permissions.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val permissionDescription = stringResource(id = R.string.read_external_storage)
        PermissionListItem(
            hasPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false,
            permissionDescription = permissionDescription
        ) {
            Icon(
                Icons.Default.Storage,
                contentDescription = stringResource(id = R.string.read_external_storage)
            )
        }
    }
    }
}



@RequiresApi(TIRAMISU)
fun permissionsMenuItemsTiramisu(
    lazyListScope : LazyListScope,
    permissionsProvider : () -> Map<String, Boolean> = { emptyMap() },
) {
    val permissions = permissionsProvider()
    lazyListScope.item {
        // Allow Notifications
        val hasNotificationPermissions =
            permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
        val notificationDescription = stringResource(id = R.string.allow_notifications)
        PermissionListItem(
            hasPermission = hasNotificationPermissions,
            permissionDescription = notificationDescription
        ) {
            Icon(
                Icons.Default.Storage,
                contentDescription = notificationDescription
            )
        }
    }


    lazyListScope.item {
        // Allow Music Playback
        val hasMusicPlaybackPermissions = permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        val readMediaAudioDescription = stringResource(id = R.string.allow_audio_playback)
        PermissionListItem(
            hasPermission = hasMusicPlaybackPermissions,
            permissionDescription = readMediaAudioDescription
        ) {
            Icon(
                Icons.Default.MusicNote,
                contentDescription = readMediaAudioDescription
            )
        }
    }

    lazyListScope.item {
        // Allow Album Art Display
        val hasImageDisplayPermissions = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val readMediaImagesDescription = stringResource(id = R.string.allow_image_display)
        PermissionListItem(
            hasPermission = hasImageDisplayPermissions,
            permissionDescription = readMediaImagesDescription) {
            Icon(
                Icons.Default.PhotoLibrary,
                contentDescription = readMediaImagesDescription
            )
        }
    }
}

class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true,
        false,
    )
}

@Preview
@Composable
private fun PermissionListItem(
    @PreviewParameter(BooleanPreviewParameterProvider::class)hasPermission: Boolean,
    permissionDescription: String = "Read External Storage",
    icon: @Composable () -> Unit = {
        Icon(
            Icons.Default.Storage,
            contentDescription = ""
        )
    }
) {
    val containerColor = if (!hasPermission) colorScheme.surfaceVariant else colorScheme.surface
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = icon,
        headlineContent = { Text(text = permissionDescription) },
        colors = ListItemDefaults.colors(containerColor = containerColor),
        trailingContent = {
            val icon = if (hasPermission) Icons.Filled.Check else Icons.Filled.Close
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = if (hasPermission) colorScheme.primary else colorScheme.error
            )
        }
    )
}

@Composable
@Preview( uiMode = UI_MODE_NIGHT_YES)
fun PreviewColorScheme() {
    val colors = arrayOf(
        Pair("primary", colorScheme.primary),
        Pair("onPrimary", colorScheme.onPrimary),
        Pair("primaryContainer", colorScheme.primaryContainer),
        Pair("onPrimaryContainer", colorScheme.onPrimaryContainer),
        Pair("inversePrimary", colorScheme.inversePrimary),
        Pair("secondary", colorScheme.secondary),
        Pair("onSecondary", colorScheme.onSecondary),
        Pair("secondaryContainer", colorScheme.secondaryContainer),
        Pair("onSecondaryContainer", colorScheme.onSecondaryContainer),
        Pair("tertiary", colorScheme.tertiary),
        Pair("onTertiary", colorScheme.onTertiary),
        Pair("tertiaryContainer", colorScheme.tertiaryContainer),
        Pair("onTertiaryContainer", colorScheme.onTertiaryContainer),
        Pair("background", colorScheme.background),
        Pair("onBackground", colorScheme.onBackground),
        Pair("surface", colorScheme.surface),
        Pair("onSurface", colorScheme.onSurface),
        Pair("surfaceVariant", colorScheme.surfaceVariant),
        Pair("onSurfaceVariant", colorScheme.onSurfaceVariant),
        Pair("surfaceTint", colorScheme.surfaceTint),
        Pair("inverseSurface", colorScheme.inverseSurface),
        Pair("inverseOnSurface", colorScheme.inverseOnSurface),
        Pair("error", colorScheme.error),
        Pair("onError", colorScheme.onError),
        Pair("errorContainer", colorScheme.errorContainer),
        Pair("onErrorContainer", colorScheme.onErrorContainer),
        Pair("outline", colorScheme.outline),
        Pair("outlineVariant", colorScheme.outlineVariant),
        Pair("scrim", colorScheme.scrim)
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(count = colors.size) {
            idx ->
            ListItem(headlineContent = {Text(colors[idx].first) },
                colors = ListItemDefaults.colors(containerColor = colorScheme.surfaceVariant),
                leadingContent = { Box(Modifier.width(60.dp).height(60.dp).background(colors[idx].second)) })

        }
    }

}

