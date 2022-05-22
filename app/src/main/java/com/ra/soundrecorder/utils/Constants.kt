package com.ra.soundrecorder.utils

import android.app.Application
import android.content.Context
import com.ra.soundrecorder.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class RecordServiceEvent {
    object PLAY: RecordServiceEvent()
    object STOP: RecordServiceEvent()
}

private const val FILENAME_FORMAT = "hh:mm:ss"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createRecordFile(application: Application): File {
    var count = 0
    var resultFile: File
    do {
        count++
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.file_record)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        resultFile = File(outputDirectory, "rec-${count}.mp4")
    } while (resultFile.exists() && !resultFile.isDirectory)

    return resultFile
}