@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
package com.github.goldy1992.mp3player.client.ui.screens.settings

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.AboutDialog
import com.github.goldy1992.mp3player.client.ui.components.FeatureRequestDialog
import com.github.goldy1992.mp3player.client.ui.components.FeedbackDialog
import com.github.goldy1992.mp3player.client.ui.components.Language
import com.github.goldy1992.mp3player.client.ui.components.LanguageSelectionDialog
import com.github.goldy1992.mp3player.client.ui.components.ReportABugDialog
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
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    viewModel : SettingsScreenViewModel = viewModel(),
    navController : NavController = rememberAnimatedNavController(),
    scope : CoroutineScope = rememberCoroutineScope(),
    windowSize: WindowSize = WindowSize.Compact
) {

    val settings by viewModel.settings.collectAsState()
    val permissions by viewModel.permissionState.collectAsState()

    val settingsOnClickMap = EnumMap<Settings.Type, Any>(Settings.Type::class.java)
    settingsOnClickMap[Settings.Type.DARK_MODE] = { newDarkMode : Boolean ->
        viewModel.setDarkMode(newDarkMode)
    }
    settingsOnClickMap[Settings.Type.USE_SYSTEM_DARK_MODE] = { useSystemDarkMode : Boolean -> viewModel.setUseSystemDarkMode(useSystemDarkMode)}
    settingsOnClickMap[Settings.Type.THEME] = { newTheme : Theme -> viewModel.setTheme(newTheme)}
    settingsOnClickMap[Settings.Type.DYNAMIC_COLOR] = { useDynamicColor : Boolean -> viewModel.setUseDynamicColor(useDynamicColor)}
    settingsOnClickMap[Settings.Type.LANGUAGE] = { language : String ->
        Log.d(logTag, "settingsOnClickMap() setting language to $language")
        viewModel.setLanguage(language)
    }


    val isLargeScreen = windowSize == WindowSize.Expanded

    if (isLargeScreen) {
        LargeSettingsScreen(
            navController = navController,
            scope = scope,
            settingsProvider = { settings},
            permissionsProvider = {permissions},
            settingsOnClickMap = settingsOnClickMap)
    } else {
        SmallSettingsScreen(
            navController = navController,
            scope = scope,
            settingsProvider = { settings},
            permissionsProvider = {permissions},
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
                        settingsOnClickMap = settingsOnClickMap,
                        modifier = Modifier.padding(it)
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
                        settingsOnClickMap = settingsOnClickMap,
                        modifier = Modifier.padding(it),
                    )
                }
            })
    }
}


@Suppress("UNCHECKED_CAST")
@Composable
fun SettingsScreenContent(
    settingsOnClickMap : EnumMap<Settings.Type, Any>,
    modifier: Modifier = Modifier,
    settingsProvider: () -> Settings = {Settings()},
    permissionsProvider: () -> Map<String, Boolean>,
    versionUtils: VersionUtils = VersionUtils(LocalContext.current)) {

    val context = LocalContext.current
    val listDescription = stringResource(id = R.string.settings_screen_list_description)
    val settings = settingsProvider()
    LazyColumn(modifier = modifier.semantics {
        contentDescription = listDescription
    }) {
        item {
            SubHeader(title = stringResource(id = R.string.display))
        }
        item {
            SystemDarkModeMenuItem(
                useSystemDarkMode = settings.useSystemDarkMode,
                onUpdate = settingsOnClickMap[Settings.Type.USE_SYSTEM_DARK_MODE] as (Boolean) -> Unit
            )
        }
        item {
            DarkModeMenuItem(
                useSystemDarkMode = settings.useSystemDarkMode,
                isdarkModePreferencesValue = settings.darkMode,
                onUpdate = settingsOnClickMap[Settings.Type.DARK_MODE] as (Boolean) -> Unit
            )
        }
        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            item {
                DynamicColorMenuItem(
                    useDynamicColor = settings.dynamicColor,
                    onUpdate = settingsOnClickMap[Settings.Type.DYNAMIC_COLOR] as (Boolean) -> Unit
                )
            }
        }

        item {
            Divider()
        }
        item {
            SubHeader(title = stringResource(id = R.string.permissions))
        }
        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            permissionsMenuItemsTiramisu(
                lazyListScope = this,
                permissionsProvider = permissionsProvider,
            )
        } else {
            permissionsMenuItemsPreTiramisu(
                lazyListScope = this,
                permissionsProvider = permissionsProvider,
            )

        }
        item {
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = ""
                    )
                },
                headlineContent = { Text(stringResource(id = R.string.update_permissions)) },
                supportingContent = { Text("Opens System Settings in order to select and refine the App Permissions.") },
                modifier = Modifier.clickable(enabled = true) {
                    val intent =
                        Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = uri
                    context.startActivity(intent, Bundle())
                }
            )
        }
        item {
            Divider()
        }
        item {
            SubHeader(title = stringResource(id = R.string.input))
        }
        item {
            LanguageMenuItem(
                currentLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en",
            )
        }

        item {
            Divider()
        }

        item {
            SubHeader(title = stringResource(id = R.string.contribute))
        }

        item {
            ReportBugMenuItem()
        }
        item {
            FeatureRequestMenuItem()
        }
        item {
            FeedbackMenuItem()
        }

        item {
            Divider()
        }

        item {
            SubHeader(title  = stringResource(id = R.string.app_info))
        }

        item {
            AboutMenuItem()
        }
        item {
            VersionMenuItem(versionUtils = versionUtils)
        }

    }

}


@Preview
@Composable
private fun SubHeader(title : String = "Permissions",
                      subtitle : String? = null) {

    val subtitleComposable : (@Composable () -> Unit)? = if (subtitle == null) null else { { Text(subtitle)} }
    ListItem(
        headlineContent = { Text(title, style = MaterialTheme.typography.titleSmall) },
        supportingContent = subtitleComposable,
        modifier = Modifier
            .fillMaxWidth()
    )
}



@RequiresApi(S)
@TargetApi(S)
@Composable
fun DynamicColorMenuItem(useDynamicColor : Boolean = true,
                        onUpdate : (newValue : Boolean) -> Unit = {}) {
    val dynamicColorDescr = stringResource(id = R.string.dynamic_color)
    ListItem(
        leadingContent = {  Icon(Icons.Filled.Palette, contentDescription = dynamicColorDescr) },
        headlineContent = { Text(dynamicColorDescr)},
        trailingContent = {
            Switch(
                checked = useDynamicColor,
                onCheckedChange = { isChecked -> onUpdate(isChecked) },
                colors = SwitchDefaults.colors(),
                modifier = Modifier.semantics { contentDescription =  dynamicColorDescr }
            )
        }
    )
}

@Composable
private fun SystemDarkModeMenuItem(useSystemDarkMode : Boolean,
                                   onUpdate : (newValue : Boolean) -> Unit = {_ ->}) {
    val switchDescription = stringResource(id = R.string.system_dark_mode_switch)
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = stringResource(id = R.string.system_dark_mode_icon)) },
        headlineContent = { Text(text = stringResource(id = R.string.use_system_dark_mode))},
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
private fun DarkModeMenuItem(isdarkModePreferencesValue : Boolean,
                             useSystemDarkMode: Boolean,
                             onUpdate: (newValue: Boolean) -> Unit) {
    val switchDescription = stringResource(id = R.string.dark_mode_switch)
    ListItem(modifier = Modifier.fillMaxWidth(),
        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = stringResource(id = R.string.dark_mode_icon)) },
        headlineContent = { Text(text = stringResource(id = R.string.dark_mode)) },
        trailingContent = {
            Switch(
                checked = isdarkModePreferencesValue,
                enabled = !useSystemDarkMode,
                onCheckedChange = { isChecked -> onUpdate(isChecked) },
                modifier = Modifier.semantics { contentDescription = switchDescription })
        })

}

@Composable
private fun ReportBugMenuItem() {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        ReportABugDialog {
            openDialog = false
        }
    }

    val reportBugText = stringResource(id = R.string.report_bug)
    ListItem(
        modifier = Modifier.clickable {
            openDialog = true
        },
        leadingContent = { Icon(Icons.Filled.BugReport, contentDescription = reportBugText) },
        headlineContent = {
            Column {
                Text(reportBugText)
            }
        },
    )
}

@Preview
@Composable
private fun FeatureRequestMenuItem() {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        FeatureRequestDialog {
            openDialog = false
        }
    }

    val featureRequestText = stringResource(id = R.string.request_feature)
    ListItem(
        modifier = Modifier.clickable {
            openDialog = true
        },
        leadingContent = { Icon(Icons.Filled.Construction, contentDescription = featureRequestText) },
        headlineContent = {
            Column {
                Text(featureRequestText)
            }
        },
    )
}

@Preview
@Composable
private fun FeedbackMenuItem() {
    var openFeedbackDialog by remember { mutableStateOf(false) }

    if (openFeedbackDialog) {
        FeedbackDialog() {
            openFeedbackDialog = false
        }
    }

    val feedbackText = stringResource(id = R.string.feedback)
    ListItem(
        modifier = Modifier.clickable {
            openFeedbackDialog = true
        },
        leadingContent = { Icon(Icons.Filled.Comment, contentDescription = feedbackText) },
        headlineContent = {
            Column {
                Text(feedbackText)
            }
        },
    )
}

@Composable
private fun VersionMenuItem(versionUtils : VersionUtils = VersionUtils(LocalContext.current)) {
    ListItem(
        leadingContent = { Spacer(modifier = Modifier.size(24.dp))},
        headlineContent = {Text(stringResource(id = R.string.version)) },
        supportingContent = { Text(versionUtils.getAppVersion(), style= MaterialTheme.typography.bodySmall) }
    )
}

@Preview
@Composable
private fun AboutMenuItem() {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        AboutDialog {
            openDialog = false
        }
    }
    ListItem(
        modifier = Modifier.clickable {
            openDialog = true
        },
        leadingContent = { Spacer(modifier = Modifier.size(24.dp))},
        headlineContent = {
            val about = stringResource(id = R.string.about)
            Text(about) 
        },
    )
}

@Composable
private fun LanguageMenuItem(currentLanguage: String = "en") {
    Log.d(logTag, "LanguageMenuItem() invoked with currentLanguage: $currentLanguage")
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss =  {
                openDialog = false
            }
        )
    }
    val languageText = stringResource(id = R.string.language)
    ListItem(
        modifier = Modifier.clickable {
            openDialog = true
        },
        leadingContent = { Icon(Icons.Default.Language, contentDescription = languageText)},
        headlineContent = {
            Text(text = languageText)
        },
        supportingContent = {
            Text(text = Language.valueOf(currentLanguage.uppercase()).writtenName)
        }
    )
}


