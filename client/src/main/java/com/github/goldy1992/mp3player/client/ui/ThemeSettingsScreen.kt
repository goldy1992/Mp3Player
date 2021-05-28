package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ThemeSelectScreen(
    navController: NavController,
    userPreferencesRepository: UserPreferencesRepository) {

    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Theme") },
            navigationIcon =  {
                NavUpButton(
                    navController = navController,
                    scope = scope) },
            backgroundColor = MaterialTheme.colors.primary)
    },
        content = {
            Column {
                ThemeSelector(
                    userPreferencesRepository = userPreferencesRepository,
                    scope = scope)            }
        })

}

@Composable
fun ThemeSelector(userPreferencesRepository : UserPreferencesRepository,
                  scope : CoroutineScope
) {

    val currentTheme by userPreferencesRepository.getTheme().collectAsState(initial = Theme.BLUE)
    Column(modifier = Modifier.selectableGroup()) {
        enumValues<Theme>().forEach { theme ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(all = DEFAULT_PADDING)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    RadioButton(selected = theme == currentTheme, onClick = {
                        scope.launch {
                            userPreferencesRepository.updateTheme(theme)
                        }
                    })
                }
                Column(modifier = Modifier
                    .weight(9f)
                    .padding(start = DEFAULT_PADDING),
                    horizontalAlignment = Alignment.Start) {
                    Text(text = theme.displayName)
                }

            }
        }
    }
}