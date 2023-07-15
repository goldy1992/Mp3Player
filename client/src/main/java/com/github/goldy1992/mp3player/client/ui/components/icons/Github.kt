package com.github.goldy1992.mp3player.client.ui.components.icons

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.LocalIsDarkMode

@Preview
@Composable
fun GithubIcon(
    modifier : Modifier = Modifier,
) {
    if (LocalIsDarkMode.current) {
        GithubIconDarkMode(modifier = modifier)
    } else {
        GithubIconLightMode(modifier = modifier)
    }
}

/**
 * For Dark Mode we use the LIGHT icon
 */
@Preview
@Composable
private fun GithubIconDarkMode(modifier : Modifier = Modifier) {

    Image(
        painterResource(id = R.drawable.github_mark_white),
        contentDescription = stringResource(id = R.string.github_dark_mode_icon_description),
        modifier = modifier
    )
}

/**
 * For Light Mode we use the Dark icon
 */
@Preview
@Composable
private fun GithubIconLightMode(modifier : Modifier = Modifier) {
    Image(
        painterResource(id = R.drawable.github_mark),
        contentDescription = stringResource(id = R.string.github_light_mode_icon_description),
        modifier = modifier
    )
}