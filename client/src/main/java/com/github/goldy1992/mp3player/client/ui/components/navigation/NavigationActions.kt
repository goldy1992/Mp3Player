package com.github.goldy1992.mp3player.client.ui.components.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.github.goldy1992.mp3player.client.Screen

object NavigationActions {
    fun navigateToLibrary(navController: NavController) {
        val isLibraryScreenSelected = Screen.LIBRARY.name == navController.currentDestination?.route
        if (!isLibraryScreenSelected) {
            navController.navigate(Screen.LIBRARY.name) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }

    fun navigateToSearch(navController: NavController) {
        val isSearchScreenSelected = Screen.SEARCH.name == navController.currentDestination?.route
        if (!isSearchScreenSelected) {
            navController.navigate(Screen.LIBRARY.name) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }
}

//        navController.navigate(JetnewsDestinations.HOME_ROUTE) {
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
//            // Avoid multiple copies of the same destination when
//            // reselecting the same item
//            launchSingleTop = true
//            // Restore state when reselecting a previously selected item
//            restoreState = true
//        }
 //   }