package com.ra.soundrecorder.utils

import android.app.Application
import android.content.Context
import com.ra.soundrecorder.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class RecordServiceEvent {
    object PLAY: RecordServiceEvent()
    object PAUSE: RecordServiceEvent()
    object STOP: RecordServiceEvent()
}

private const val FILENAME_FORMAT = "hh:mm:ss"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.file_record)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.mp4")
}

fun getTimeStringFormat(
    context: Context,
    i: Int
): String {
    val minute = i / 60
    return String.format(context.getString(R.string.time_format_mm_ss, minute, i))
}