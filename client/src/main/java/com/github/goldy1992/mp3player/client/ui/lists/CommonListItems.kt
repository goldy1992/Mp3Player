package com.github.goldy1992.mp3player.client.ui.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.utils.MediaItemNameUtils
import com.github.goldy1992.mp3player.commons.MediaItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoResultsFound(mediaItemType: MediaItemType,
                    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mediaItemTypeTitle = MediaItemNameUtils.getMediaItemTypeName(context, mediaItemType)
    val noResultsText = stringResource(id = R.string.no_results, mediaItemTypeTitle)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(DEFAULT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ListItem(
            leadingContent = {
                Icon(Icons.Default.Info, contentDescription = noResultsText)
            },
            headlineText = {
                Text(text = noResultsText)
            }
        )
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoPermissions(
        modifier: Modifier = Modifier
    ) {
        val noPermissionsText = stringResource(id = R.string.no_permissions)
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(DEFAULT_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        ListItem(
            leadingContent = {
                Icon(Icons.Default.Warning, contentDescription = noPermissionsText)
            },
            headlineText = {
                Text(text = noPermissionsText)
            }
        )
    }
}

