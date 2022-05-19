package com.ra.soundrecorder.utils

sealed class RecordService {
    object PLAY: RecordService()
    object STOP: RecordService()
}



