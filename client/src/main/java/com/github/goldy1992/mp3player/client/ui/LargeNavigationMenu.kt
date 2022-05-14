package com.github.goldy1992.mp3player.client.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Screen

/*
val SMALL_WIDTH: Dp = 80.dp
val LARGE_WIDTH: Dp = 256.dp

enum class MenuState { Expanded, Collapsed }

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun LargeNavigationMenu(navController: NavController = rememberNavController(),
expanded : MutableState<MenuState> = mutableStateOf(MenuState.Collapsed)) {
    val width: Dp by animateDpAsState(targetValue = if (expanded.value == MenuState.Collapsed) SMALL_WIDTH else LARGE_WIDTH,
    animationSpec = tween(200, 0, LinearEasing))

    if (expanded.value == MenuState.Collapsed) {
        CollapsedNavigationMenu(width = width,
        navController = navController) {
            expanded.value = MenuState.Expanded
        }
    } else {
        LargeNavigationMenu(
            navController = navController,
            width = width) {
            expanded.value = MenuState.Collapsed
        }
    }
}


@Preview
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LargeNavigationMenu(width: Dp = LARGE_WIDTH,
                        navController: NavController = rememberNavController(),
                        onClick: () -> Unit = {}) {

    Surface(color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(width = width)
            .fillMaxHeight()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()) {

                Text(modifier = Modifier.padding(start = 10.dp),
                        text = "Menu", style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface)


            Divider()
            LibraryItem(navController = navController)
            SettingsItem(navController = navController)
        }
    }




}

@Preview
@Composable
private fun CollapsedNavigationMenu(
    width : Dp = SMALL_WIDTH,
    navController : NavController = rememberNavController(),
    onClick: () -> Unit = {}) {
    NavigationRail(
        backgroundColor = MaterialTheme.colors.primarySurface,
        modifier = Modifier
            .width(width = width)
            .fillMaxHeight(),
        header = {
            IconButton(onClick = {
                onClick()
            }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        content = {
            val settings = stringResource(id = R.string.settings)
            NavigationRailItem(selected = false,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Filled.Settings, contentDescription = settings) },
                label = { Text(settings) },
                modifier = Modifier.clickable { navController.navigate(Screen.SETTINGS.name) }
            )
    }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun dnd() {
    DismissibleNavigationDrawer(drawerContent = {

        Text("HelloWOrld")
    }) {

    }
}*/

