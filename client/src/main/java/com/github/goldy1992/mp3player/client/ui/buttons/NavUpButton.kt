package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back),
        tint = MaterialTheme.colorScheme.onSurface)
    }
}