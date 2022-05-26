package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@ExperimentalMaterialApi
@Composable
fun ThemeSelector(userPreferencesRepository : UserPreferencesRepository,
                  scope : CoroutineScope
) {

    val currentTheme by userPreferencesRepository.getTheme().collectAsState(initial = Theme.BLUE)
    Column(modifier = Modifier
        .selectableGroup()
        .fillMaxWidth()
        .padding(all = DEFAULT_PADDING)) {
        enumValues<Theme>().forEach { theme ->
            ListItem(
                text = { Text(text = theme.displayName) },
                icon = {
                    RadioButton(selected = theme == currentTheme, onClick = {
                        scope.launch {
                            userPreferencesRepository.updateTheme(theme)
                        }
                    })
                })
        }
    }
}