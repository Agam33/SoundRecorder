package com.ra.soundrecorder.model

data class SoundRecord(
    var id: Int = 0,
    var name: String? = "",
    var duration: Long = 0,
    var filePath: String? = "",

)
