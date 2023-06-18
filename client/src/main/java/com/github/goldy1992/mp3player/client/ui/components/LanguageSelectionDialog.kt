package com.github.goldy1992.mp3player.client.ui.components

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.github.goldy1992.mp3player.client.R

private const val LOG_TAG = "LANGUAGE_SELECTION_DIALOG"
enum class Language(
    val writtenName : String,
    val code : String
) {

    EN("English", "en"),
    ES("Español", "es"),
    TH("ไทย", "th")
}



@Composable
fun LanguageSelectionDialog(
    currentLanguage: String = "en",
    onDismiss : () -> Unit = {}
) {
    val selectLanguageTitle = stringResource(id = R.string.select_language)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(getInitialLanguage(currentLanguage)) }
    Log.d(LOG_TAG, "initial selected option: $selectedOption")
    AlertDialog(
        title = { Text(selectLanguageTitle) },
        confirmButton = {
            val selectedLanguage = selectedOption.code.lowercase()
            TextButton(onClick =
            {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(
                        selectedLanguage
                    )
                )
                onDismiss()
                Log.d(LOG_TAG, "dialog select pressed selected_language: $selectedLanguage")
            }) {
                Text(text = "Select")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column(Modifier.selectableGroup()) {
                Language.values().forEach { text ->
                    Row(
                        Modifier
                            // .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    Log.d(LOG_TAG, "$text was selected")
                                    onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null, // null recommended for accessibility with screenreaders
                            enabled = text.code != "th"
                        )
                        Text(
                            text = text.writtenName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

            }
        },
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        onDismissRequest = { onDismiss() }
    )


}



private fun getInitialLanguage(currentLanguage : String) : Language {
    return try {
        Language.valueOf(currentLanguage.uppercase())
    } catch (ex : IllegalArgumentException) {
        Log.e(LOG_TAG, "did not find a matching language for the code $currentLanguage, will use Initial Language as English")
        Language.EN
    }
}