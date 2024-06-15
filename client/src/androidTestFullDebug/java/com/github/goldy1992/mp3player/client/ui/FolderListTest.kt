package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Test for the [FolderList]
 */
class FolderListTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDisplayFolderList() {
        val folder1Name = "folder name1"
        val folder1Path = "/a/b/${folder1Name}"
        val folder1 = Folder(name = folder1Name,
                          path = folder1Path,

        )
        val folder2Name = "folder name2"
        val folder2Path = "/a/b/${folder2Name}"
        val folder2 = Folder(name = folder2Name, path = folder2Path)

        composeTestRule.setContent {
            FolderList(
                folders = Folders(state = State.LOADED, folders = listOf(folder1, folder2))
            )
        }
        val folderListDescr = context.getString(R.string.folder_list)
        val node = composeTestRule.onNodeWithContentDescription(folderListDescr)
        val errorMessage : (String) -> String = { "Could not find $it in semantic tree"}
        composeTestRule.onNodeWithText(folder1Name).assertExists(errorMessage(folder1Name))
        composeTestRule.onNodeWithText(folder1Path).assertExists(errorMessage(folder1Path))
        composeTestRule.onNodeWithText(folder2Name).assertExists(errorMessage(folder2Name))
        composeTestRule.onNodeWithText(folder2Path).assertExists(errorMessage(folder2Path))

        val children = node.fetchSemanticsNode().children
        Assert.assertEquals(2, children.size)
    }

}