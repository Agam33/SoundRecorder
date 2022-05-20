package com.ra.soundrecorder.service

import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asLiveData
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.model.SoundRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
class RecordingService: LifecycleService() {

    @Inject
    lateinit var repository: SoundRecordRepository

    private var mFilePath: File? = null
    private var mFileName: String? = null

    private var mediaRecorder: MediaRecorder? = null

    private var mStartingTimeMillis: Long = 0

    private val coroutineScope = CoroutineScope(IO)

    override fun onCreate() {
        (application as App).mainComponent.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent.let {
            when(it?.action) {
                START_SERVICE -> {
                    observer()
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
            setOutputFile(mFilePath)
        }

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            mStartingTimeMillis = System.currentTimeMillis()
        } catch (e: Exception) {
            Timber.d("Error: $e")
        }
    }

    private fun observer() {
        repository
            .getAllRecord()
            .asLiveData()
            .observe(this) { setFilePathAndName(it.size) }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder = null
        mediaRecorder?.release()

        val soundRecord = SoundRecord(
            name = mFileName,
            filePath = mFilePath?.path,
            duration = (System.currentTimeMillis() - mStartingTimeMillis)
        )

        Timber.d("File saved to ${mFilePath?.path}")

        coroutineScope.launch { repository.insertRecord(soundRecord) }
    }

    private fun setFilePathAndName(recordSize: Int) {
        var count = 0

        do {
            count++

            mFileName = String.format(getString(R.string.default_file_name), recordSize + count)

            val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
                File(it, getString(R.string.app_name)).apply { mkdirs() }
            }

            mFilePath = if(
                mediaDir != null && mediaDir.exists()
            ) mediaDir else application.filesDir

            val file = File(mFilePath, mFileName ?: "")
        } while(file.exists())
    }

    companion object {
        const val START_SERVICE = "start-service"
        const val STOP_SERVICE = "stop-service"
    }
}