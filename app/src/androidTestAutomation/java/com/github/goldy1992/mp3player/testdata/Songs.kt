package com.github.goldy1992.mp3player.testdata

import com.github.goldy1992.mp3player.testdata.TestMetadata.DPRIMERA_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.HABANA_MIA_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.HAVANA_DE_PRIMERA
import com.github.goldy1992.mp3player.testdata.TestMetadata.LA_MITAD_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.LA_MUJER_PIROPO_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.MUJERIEGO_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.PASTILLA_DE_MENTA_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.REGALITO_DE_DIOS_TITLE
import com.github.goldy1992.mp3player.testdata.TestMetadata.VENENOSA_TITLE

object Songs {

    // SONGS
    val LA_MITAD : Song = SongBuilder()
            .title(LA_MITAD_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val VENENOSA : Song = SongBuilder()
            .title(VENENOSA_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val REGALITO_DE_DIOS : Song = SongBuilder()
            .title(REGALITO_DE_DIOS_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val DPRIMERA : Song = SongBuilder()
            .title(DPRIMERA_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val LA_MUJER_PIROPO : Song = SongBuilder()
            .title(LA_MUJER_PIROPO_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val MUJERIEGO : Song = SongBuilder()
            .title(MUJERIEGO_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val PASTILLA_DE_MENTA : Song = SongBuilder()
            .title(PASTILLA_DE_MENTA_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()

    val HABANA_MIA : Song = SongBuilder()
            .title(HABANA_MIA_TITLE)
            .artist(HAVANA_DE_PRIMERA)
            .build()


}