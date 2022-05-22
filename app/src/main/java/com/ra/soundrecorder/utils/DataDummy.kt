package com.ra.soundrecorder.utils

import com.ra.soundrecorder.model.SoundRecord

object DataDummy {
    fun getAllRecord(): List<SoundRecord> =
        ArrayList<SoundRecord>().apply {
            for(i in 0..10) {
               add( SoundRecord(
                    i,
                    "rec-$i",
                    1000,
                    ""
                ))
            }
        }
}