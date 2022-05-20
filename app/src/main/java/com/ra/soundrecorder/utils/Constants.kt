package com.ra.soundrecorder.utils

import android.content.Context
import com.ra.soundrecorder.R

sealed class RecordServiceEvent {
    object PLAY: RecordServiceEvent()
    object PAUSE: RecordServiceEvent()
    object STOP: RecordServiceEvent()
}

fun getTimeStringFormat(
    context: Context,
    i: Int
): String {
    val hours = i / 3600
    val minute = i / 60
    return String.format(context.getString(R.string.time_format_hh_mm_ss, hours, minute, i))
}