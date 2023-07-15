package com.github.goldy1992.mp3player.client.ui.components.navigation.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.FeedbackDialog
import com.github.goldy1992.mp3player.client.ui.components.RatingDialog
import com.github.goldy1992.mp3player.client.ui.utils.RatingUtils.submit

@Composable
fun ReviewNavigationItem() {
    val context = LocalContext.current
    var openRatingDialog by remember { mutableStateOf(false) }
    var openFeedbackDialog by remember { mutableStateOf(false) }

    if (openRatingDialog) {
        RatingDialog {rating : Int? ->
            if (rating != null) {
                if (rating >= 5) {
                    submit(context)
                } else {
                    openFeedbackDialog = true
                }
            }
            openRatingDialog = false
        }
    }

    if (openFeedbackDialog) {
        FeedbackDialog {
            openFeedbackDialog = false
        }

    }
    NavigationDrawerItem(modifier = Modifier
        .padding(horizontal = 12.dp),
        label = {
            Text(
                text = stringResource(id = R.string.review),
                style = MaterialTheme.typography.labelLarge
            )
        },
        icon = {
            Icon(
                Icons.Filled.Star,
                contentDescription = "Equalizer",
            )
        },
        selected = false,

        onClick = {
            if (!openRatingDialog) {
                openRatingDialog = true
            }
        }
    )

}