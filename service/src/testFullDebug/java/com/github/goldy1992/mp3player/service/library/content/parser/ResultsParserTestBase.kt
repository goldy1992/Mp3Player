package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.MatrixCursor
import androidx.media3.common.MediaItem
import org.robolectric.fakes.RoboCursor
import java.util.*

abstract class ResultsParserTestBase {
    var resultsParser: ResultsParser? = null
    open fun setup() {}
    abstract fun testGetType()
    abstract fun createDataSet(): Array<Array<Any?>?>
    fun getResultsForProjection(projection: Array<String?>): List<MediaItem> {
        val cursor = MatrixCursor(projection)
        val dataset = createDataSet()
        for (row in dataset) {
            cursor.addRow(row)
        }
        return resultsParser!!.create(cursor)
    }
}