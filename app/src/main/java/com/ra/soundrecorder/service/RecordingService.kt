package com.ra.soundrecorder.service

import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import com.ra.soundrecorder.data.SoundRecordRepository
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
class RecordingService: LifecycleService() {

    @Inject
    lateinit var soundRecordRepository: SoundRecordRepository

    private var mFilePath: String? = null
    private val mFileName: String? = null

    private var mediaRecorder: MediaRecorder? = null

    private var mStartingTimeMillis: Long = 0



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent.let {
            when(it?.action) {
                START_SERVICE -> {
                    startRecording()
                }
                STOP_SERVICE -> {
                    stopRecording()
                }
            }
        }
        return START_STICKY
    }


    private fun startRecording() {

        mediaRecorder = MediaRecorder(baseContext).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(44100)
            setAudioEncodingBitRate(192000)
            setOutputFile("") // TODO: output file
        }

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
        } catch (e: Exception) {
            Timber.d("Error: $e")
        }

    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder = null
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }


    private fun setFilePathAndName() {
        do {
            // TODO: create fileName and path
            val file = File("")
        } while(!file.exists() && file.isDirectory)
    }

    companion object {
        const val START_SERVICE = "start-service"
        const val STOP_SERVICE = "stop-service"
    }

}