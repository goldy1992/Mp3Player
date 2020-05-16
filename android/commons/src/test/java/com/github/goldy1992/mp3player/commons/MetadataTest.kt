package com.github.goldy1992.mp3player.commons

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class MetadataTest {

    val mapper = ObjectMapper()

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun testMetadataEmpty() {
       val testValue : Metadata =  Metadata.Builder()
               .build()
        val expected = "{" +
                        "\"id\":null," +
                        "\"title\":\"${Constants.UNKNOWN}\"," +
                        "\"artist\":\"${Constants.UNKNOWN}\"," +
                        "\"duration\":0," +
                        "\"albumArtPath\":null" +
                        "}"
       val result : String =  mapper.writeValueAsString(testValue)

        assertEquals(expected, result)
    }


}