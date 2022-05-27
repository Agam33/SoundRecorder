package com.ra.soundrecorder.utils

import android.app.Application
import android.content.Context
import com.ra.soundrecorder.R
import com.ra.soundrecorder.model.SoundRecord
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class RecordServiceEvent {
    object PLAY: RecordServiceEvent()
    object STOP: RecordServiceEvent()
}

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

fun deleteFile(filePath: String) {
    val file = File(filePath)
    file.delete()
}

fun updateFile(
    newName: String,
    soundRecord: SoundRecord,
    application: Application,
    setChange: (SoundRecord) -> Unit = {}
) {

    val mediaDir = application.externalMediaDirs.first()

    val oldFile = File(soundRecord.filePath ?: "")
    val newFile = File(mediaDir, "$newName.mp4")
    oldFile.renameTo(newFile)
    val newSoundRecord = SoundRecord(
        soundRecord.id,
        newName,
        soundRecord.duration,
        newFile.absolutePath
    )
    setChange(newSoundRecord)
}