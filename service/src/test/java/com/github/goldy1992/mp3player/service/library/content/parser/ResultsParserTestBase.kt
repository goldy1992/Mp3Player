package com.github.goldy1992.mp3player.service.library.content.parser

import android.support.v4.media.MediaBrowserCompat
import org.robolectric.fakes.RoboCursor
import java.util.*

abstract class ResultsParserTestBase {
    var resultsParser: ResultsParser? = null
    open fun setup() {}
    abstract fun testGetType()
    abstract fun createDataSet(): Array<Array<Any?>>
    fun getResultsForProjection(projection: Array<String?>, testPrefix: String?): List<MediaBrowserCompat.MediaItem> {
        val cursor = RoboCursor()
        val columns = Arrays.asList<String>(*projection)
        cursor.setColumnNames(columns)
        cursor.setResults(createDataSet())
        return resultsParser!!.create(cursor, testPrefix!!)
    }
}