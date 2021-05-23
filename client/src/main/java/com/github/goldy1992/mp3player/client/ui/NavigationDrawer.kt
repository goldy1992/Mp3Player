package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R

@ExperimentalMaterialApi
@Preview
@Composable
fun NavigationDrawer() {
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
            ListItem(icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                text = { Text("Settings")},

            )

        }


    }

}