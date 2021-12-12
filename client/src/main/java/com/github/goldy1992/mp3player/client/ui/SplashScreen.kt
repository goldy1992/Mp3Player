package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SplashScreen(
    navController: NavController,
    userPreferencesRepository: UserPreferencesRepository
) {
    AppTheme(userPreferencesRepository = userPreferencesRepository) {
        SplashScreenContent()
    }
    LaunchedEffect(true) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(5000)
            navController.popBackStack()
            navController.navigate(Screen.MAIN.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenContent() {

    Column(
            Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.headphone_icon),
            contentDescription = stringResource(id = R.string.splash_screen_icon)
        )
        Text(text = stringResource(id = R.string.app_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground

        )
    }
}

