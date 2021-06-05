package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavUpButton(navController : NavController,
                scope : CoroutineScope) {
    IconButton(onClick = {
        scope.launch {
            navController.popBackStack()
        }
    }){
        Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.back))
    }
}