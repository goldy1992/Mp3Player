package com.github.goldy1992.mp3player.client.ui.components.navigation

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.LibraryNavigationRailItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.SearchNavigationRailItem
import com.github.goldy1992.mp3player.commons.Screen

@Preview
@Composable
fun AppNavigationRail(navController: NavController = rememberNavController(),
                      currentScreen : Screen = Screen.LIBRARY,
                      context : Context = LocalContext.current,
                      onClickMenuIcon : () -> Unit = {}) {

    NavigationRail(
        header = {
            IconButton(
                modifier = Modifier.padding(vertical = 12.dp),
                onClick = onClickMenuIcon,
            )    {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
            }
        },
    ){
        Spacer(Modifier.weight(1f))
        val isLibraryScreenSelected = currentScreen == Screen.LIBRARY
        LibraryNavigationRailItem(isSelected = isLibraryScreenSelected) {
              NavigationActions.navigateToLibrary(navController)
        }

        SearchNavigationRailItem(isSelected = currentScreen == Screen.SEARCH) {
            NavigationActions.navigateToSearch(navController)
        }
        Spacer(Modifier.weight(1f))
    }

}