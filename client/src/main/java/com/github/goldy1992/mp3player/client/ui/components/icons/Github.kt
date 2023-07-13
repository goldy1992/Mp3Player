package com.github.goldy1992.mp3player.client.ui.components.icons

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R

@Preview
@Composable
fun GithubIcon(
    modifier : Modifier = Modifier,
    isDarkMode : Boolean = false) {
    Image(
        painterResource(id = if (isDarkMode) R.drawable.github_mark_white else R.drawable.github_mark),
        contentDescription = "",
        modifier = modifier
    )
}