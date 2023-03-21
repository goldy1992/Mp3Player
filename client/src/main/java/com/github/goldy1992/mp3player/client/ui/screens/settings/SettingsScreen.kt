@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
package com.github.goldy1992.mp3player.client.ui.screens.settings

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.utils.VersionUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val logTag = "SettingsScreen"


/**
 * The Settings Screen
 */
@Composable
fun SettingsScreen(
    viewModel : SettingsScreenViewModel = viewModel(),
    navController : NavController,
    scope : CoroutineScope = rememberCoroutineScope(),
    windowSize: WindowSize = WindowSize.Compact
) {

    val settings by viewModel.settings.collectAsState()
    val permissions by viewModel.permissionState.collectAsState()
    val requestPermission = { permission : String  -> viewModel.requestPermission(permission)}

    val settingsOnClickMap = EnumMap<Settings.Type, Any>(Settings.Type::class.java)
    settingsOnClickMap[Settings.Type.DARK_MODE] = { newDarkMode : Boolean -> viewModel.setDarkMode(newDarkMode)}
    settingsOnClickMap[Settings.Type.USE_SYSTEM_DARK_MODE] = { useSystemDarkMode : Boolean -> viewModel.setUseSystemDarkMode(useSystemDarkMode)}
    settingsOnClickMap[Settings.Type.THEME] = { newTheme : Theme -> viewModel.setTheme(newTheme)}

    val isLargeScreen = windowSize == WindowSize.Expanded

    if (isLargeScreen) {
        LargeSettingsScreen(
            navController = navController,
            scope = scope,
            settingsProvider = { settings},
            permissionsProvider = {permissions},
            requestPermission = requestPermission,
            settingsOnClickMap = settingsOnClickMap)
    } else {
        SmallSettingsScreen(
            navController = navController,
            scope = scope,
            settingsProvider = { settings},
            permissionsProvider = {permissions},
            requestPermission = requestPermission,
            settingsOnClickMap = settingsOnClickMap)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LargeSettingsScreen(
    navController: NavController,
    scope: CoroutineScope,
    settingsProvider: () -> Settings,
    permissionsProvider: () -> Map<String, Boolean>,
    requestPermission: (String) -> Unit,
    settingsOnClickMap: EnumMap<Settings.Type, Any>
) {
    PermanentNavigationDrawer(drawerContent = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.SETTINGS
        )
    }) {

        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    NavUpButton(
                        navController = navController,
                        scope = scope
                    )
                }
            )
        },
            content = {
                Surface(Modifier.width(500.dp)) {
                    SettingsScreenContent(
                        settingsProvider = settingsProvider,
                        permissionsProvider = permissionsProvider,
                        requestPermission = requestPermission,
                        settingsOnClickMap = settingsOnClickMap,
                        modifier = Modifier.padding(it),
                        navController = navController,
                    )
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallSettingsScreen(
    settingsProvider: () -> Settings,
    permissionsProvider: () -> Map<String, Boolean>,
    requestPermission: (String) -> Unit,
    settingsOnClickMap: EnumMap<Settings.Type, Any>,
    navController: NavController,
    scope: CoroutineScope,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.SETTINGS
        )
    }) {

        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    NavUpButton(
                        navController = navController,
                        scope = scope
                    )
                }
            )
        },
            content = {
                Surface(Modifier.width(500.dp)) {
                    SettingsScreenContent(
                        settingsProvider = settingsProvider,
                        permissionsProvider = permissionsProvider,
                        requestPermission = requestPermission,
                        settingsOnClickMap = settingsOnClickMap,
                        modifier = Modifier.padding(it),
                        navController = navController
                    )
                }
            })
    }
}


@Composable
fun SettingsScreenContent(
    settingsOnClickMap : EnumMap<Settings.Type, Any>,
    modifier: Modifier = Modifier,
    settingsProvider: () -> Settings = {Settings()},
    permissionsProvider: () -> Map<String, Boolean>,
    requestPermission: (String) -> Unit,
    navController: NavController = rememberAnimatedNavController(),
    versionUtils: VersionUtils = VersionUtils(LocalContext.current)) {


    val settings = settingsProvider()
    Column(modifier = modifier) {
        Subheader(title = stringResource(id = R.string.display))
        SystemDarkModeMenuItem(
            useSystemDarkMode = settings.useSystemDarkMode,
            onUpdate = settingsOnClickMap[Settings.Type.USE_SYSTEM_DARK_MODE] as (Boolean) -> Unit
        )
        DarkModeMenuItem(
            useSystemDarkMode = settings.useSystemDarkMode,
            isDarkMode = settings.darkMode,
            onUpdate = settingsOnClickMap[Settings.Type.DARK_MODE] as (Boolean) -> Unit
        )
        // TODO: Add Dynamic color option for android 12
        Divider()
        Subheader(title = stringResource(id = R.string.permissions))
        PermissionsMenuItems(
            permissionsProvider = permissionsProvider,
            requestPermission = requestPermission)
        Divider()
        Subheader(title = stringResource(id = R.string.help))
        SupportAndFeedbackMenuItem(navController)
        Divider()
        VersionMenuItem(versionUtils = versionUtils)
    }

}


@Composable
private fun Subheader(title : String) {
    ListItem(
        headlineText = { Text(title, style = MaterialTheme.typography.titleSmall) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun PermissionsSubheader() {
    ListItem(
        headlineText = { Text(stringResource(id = R.string.permissions), style = MaterialTheme.typography.titleSmall) },
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Composable
private fun ThemeMenuItem(navController: NavController) {
    val theme = stringResource(id = R.string.theme)
    ListItem(
        leadingContent = { Icon(Icons.Filled.Palette, contentDescription = theme) },
        headlineText = { Text(theme, style = MaterialTheme.typography.titleMedium) },
        modifier = Modifier.clickable { navController.navigate(Screen.THEME_SELECT.name)}
    )
}

@Composable
private fun SystemDarkModeMenuItem(useSystemDarkMode : Boolean,
                                   onUpdate : (newValue : Boolean) -> Unit = {_ ->}) {
    val switchDescription = stringResource(id = R.string.system_dark_mode_switch)
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = stringResource(id = R.string.system_dark_mode_icon)) },
        headlineText = { Text(text = stringResource(id = R.string.use_system_dark_mode))},
        trailingContent = {
            Switch(
                checked = useSystemDarkMode,
                onCheckedChange = { isChecked -> onUpdate(isChecked) },
                colors = SwitchDefaults.colors(),
                modifier = Modifier.semantics { contentDescription =  switchDescription }
            )
        }
    )
}

@Composable
private fun DarkModeMenuItem(isDarkMode : Boolean,
                            useSystemDarkMode: Boolean,
                            onUpdate: (newValue: Boolean) -> Unit) {
    val switchDescription = stringResource(id = R.string.dark_mode_switch)
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = stringResource(id = R.string.dark_mode_icon)) },
        headlineText = { Text(text = stringResource(id = R.string.dark_mode)) },
        trailingContent = {
            Switch(
                checked = isDarkMode,
                enabled = !useSystemDarkMode,
                onCheckedChange = { isChecked -> onUpdate(isChecked) },
                modifier = Modifier.semantics { contentDescription = switchDescription })
        })

}

@Composable
private fun SupportAndFeedbackMenuItem(navController: NavController) {
    val supportAndFeedback = stringResource(id = R.string.support_and_feedback)
    ListItem(
        leadingContent = { Icon(Icons.Filled.Help, contentDescription = supportAndFeedback) },
        headlineText = {
            Column() {
                Text(supportAndFeedback)
            }
        },
    )
}

@Composable
private fun VersionMenuItem(versionUtils : VersionUtils = VersionUtils(LocalContext.current)) {
    ListItem(
        leadingContent = {},
        headlineText = {Text(stringResource(id = R.string.version)) },
        supportingText = { Text(versionUtils.getAppVersion(), style= MaterialTheme.typography.bodySmall) }
    )
}

@Composable
private fun AboutMenuItem(navController: NavController) {
    ListItem(
        leadingContent = { },
        headlineText = {
                Text("About") // TODO: Translate and link to about page!
        },
    )
}

@Composable
private fun PermissionsMenuItems(permissionsProvider : () -> Map<String, Boolean>,
    requestPermission : (String) -> Unit) {
    val context = LocalContext.current
    val permissions = permissionsProvider()

    if (permissions.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        val hasExternalStoragePermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        val switchDescription = stringResource(id = R.string.read_external_storage)
        ListItem(modifier = Modifier.fillMaxWidth(),
            leadingContent = { Icon(Icons.Default.Storage, contentDescription = stringResource(id = R.string.read_external_storage)) },
            headlineText = { Text(text = stringResource(id = R.string.read_external_storage))},
            trailingContent = {
                Switch(
                    enabled = !hasExternalStoragePermission,
                    checked = hasExternalStoragePermission,
                    colors = SwitchDefaults.colors(),
                    modifier = Modifier.semantics { contentDescription =  switchDescription },
                    onCheckedChange = {
                        Log.i(logTag, "on checked has external storage permissions")
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                )
            }
        )
    }

    Button(onClick = {
        context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS), Bundle());
    }) {
        Text(text = "Revoke Permissions")
    }

    // 1. Notifications
    val switchDescription = stringResource(id = R.string.allow_notifications)
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = { Icon(Icons.Default.Notifications, contentDescription = stringResource(id = R.string.allow_notifications)) },
        headlineText = { Text(text = stringResource(id = R.string.allow_notifications))},
        trailingContent = {
            Switch(
                enabled = false,
                checked = true,
                colors = SwitchDefaults.colors(),
                modifier = Modifier.semantics { contentDescription =  switchDescription },
                onCheckedChange = {}
            )
        }
    )
    // 2. Music and Audio
    // 3. Photos and Videos
}