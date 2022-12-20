package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.io.File

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
        val folder1Obj = File(folder1Path)
        val folder1 = MediaItemBuilder().setDirectoryFile(folder1Obj).build()

        val folder2Name = "folder name2"
        val folder2Path = "/a/b/${folder2Name}"
        val folder2Obj = File(folder2Path)
        val folder2 = MediaItemBuilder().setDirectoryFile(folder2Obj).build()

        composeTestRule.setContent {
            FolderList(listOf(folder1, folder2))
        }
        val folderListDescr = context.getString(R.string.folder_list)
        val node = composeTestRule.onNodeWithContentDescription(folderListDescr)
        node.onChildAt(0).assert(hasText(folder1Name))
        node.onChildAt(0).assert(hasText(folder1Path))
        node.onChildAt(1).assert(hasText(folder2Name))
        node.onChildAt(1).assert(hasText(folder2Path))

        val children = node.fetchSemanticsNode().children
        Assert.assertEquals(2, children.size)
    }

}