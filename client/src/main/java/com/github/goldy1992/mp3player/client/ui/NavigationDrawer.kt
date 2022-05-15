package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Screen

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun NavigationDrawer(navController: NavController,
                    modifier : Modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()) {
    Column(modifier = modifier) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.headphone_icon),
                contentDescription = "Menu icon"
            )
            Card() {
                Text(modifier = Modifier.padding(start = 10.dp), text = "Menu", style = MaterialTheme.typography.labelLarge, )
            }

            Divider()
            LibraryItem(navController = navController)
            SettingsItem(navController = navController)
        }
    }

}


@Preview
@ExperimentalMaterialApi
@Composable
fun LibraryItem(navController: NavController = rememberNavController(),
                selected : Boolean = true) {
    val library = stringResource(id = R.string.library)
    ListItem(
        icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = library) },
        text = { Text(library) },
        modifier = Modifier
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.background
            )
            .clickable { navController.navigate(Screen.MAIN.name) }
    )
}


@ExperimentalMaterialApi
@Composable
fun SettingsItem(navController: NavController) {
    val settings = stringResource(id = R.string.settings)
    ListItem(
        icon = { Icon(Icons.Filled.Settings, contentDescription = settings) },
        text = { Text(settings) },
        modifier = Modifier.clickable { navController.navigate(Screen.SETTINGS.name) }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NavigationDrawerContent(navController: NavController = rememberNavController()) {

        Image(
            modifier = Modifier.padding(vertical = 20.dp,horizontal = 12.dp)
                .size(40.dp),
            painter = painterResource(id = R.drawable.headphone_icon),
            contentDescription = "Menu icon"
        )

        val settings = stringResource(id = R.string.settings)
        NavigationDrawerItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            label = {
                Text(
                    text = settings,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            },
            icon = { Icon(Icons.Filled.Settings,
                contentDescription = settings,
                modifier = Modifier.size(24.dp)) },
            selected = false,
            onClick = { navController.navigate(Screen.SETTINGS.name) })

        val library = stringResource(id = R.string.library)
        NavigationDrawerItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            label = {
                Text(
                    text = library,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            },
            icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = library) },
            selected = true,
            onClick = {}
        )
        Divider(
            modifier = Modifier.padding(top = 16.dp,
            end = 28.dp),
            startIndent = 28.dp,
            color = MaterialTheme.colorScheme.outline)



}

