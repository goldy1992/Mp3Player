package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Screen

@ExperimentalMaterialApi
@Composable
fun NavigationDrawer(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.headphone_icon),
                contentDescription = "Menu icon"
            )
            Card() {
                Text(modifier = Modifier.padding(start = 10.dp), text = "Menu", style = MaterialTheme.typography.h6, )
            }

            Divider()
            LibraryItem(navController = navController)
            SettingsItem(navController = navController)
        }
    }

}


@ExperimentalMaterialApi
@Composable
private fun LibraryItem(navController: NavController) {
    val library = stringResource(id = R.string.library)
    ListItem(
        icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = library) },
        text = { Text(library) },
        modifier = Modifier.clickable { navController.navigate(Screen.MAIN.name) }
    )
}


@ExperimentalMaterialApi
@Composable
private fun SettingsItem(navController: NavController) {
    val settings = stringResource(id = R.string.settings)
    ListItem(
        icon = { Icon(Icons.Filled.Settings, contentDescription = settings) },
        text = { Text(settings) },
        modifier = Modifier.clickable { navController.navigate(Screen.SETTINGS.name) }
    )

}

