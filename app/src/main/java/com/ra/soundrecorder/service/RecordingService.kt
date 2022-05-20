package com.ra.soundrecorder.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.model.SoundRecord
import com.ra.soundrecorder.ui.recorder.RecorderFragment
import com.ra.soundrecorder.utils.RecordServiceEvent
import com.ra.soundrecorder.utils.getTimeStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnspecifiedImmutableFlag")
class RecordingService: LifecycleService() {

    @Inject
    lateinit var repository: SoundRecordRepository

    private var mFilePath: File? = null
    private var mFileName: String? = null

    private var mediaRecorder: MediaRecorder? = null

    private var mStartingTimeMillis: Long = 0

    private val coroutineScope = CoroutineScope(IO)

    private var isServiceRunning = true

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        (application as App).mainComponent.inject(this)
        super.onCreate()
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent.let {
            when(it?.action) {
                START_SERVICE -> {
                    observer()
                    startTimer()
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
        startForeground(NOTIFY_ID, notificationBuilder.build())
        RECORD_SERVICE.postValue(RecordServiceEvent.PLAY)

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
        RECORD_SERVICE.postValue(RecordServiceEvent.STOP)
        isServiceRunning = false
        mediaRecorder?.stop()
        mediaRecorder = null
        mediaRecorder?.release()

        val soundRecord = SoundRecord(
            name = mFileName,
            filePath = mFilePath?.path,
            duration = (System.currentTimeMillis() - mStartingTimeMillis)
        )

        Timber.d("File saved to ${mFilePath?.path}")

        Toast.makeText(baseContext, "File saved to ${mFilePath?.path}", Toast.LENGTH_SHORT).show()

        coroutineScope.launch { repository.insertRecord(soundRecord) }

        stopForeground(true)
        stopSelf()
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

    private fun startTimer() {
        coroutineScope.launch {
            var i = 1
            while (isServiceRunning) {
                delay(1000)
                val time = getTimeStringFormat(baseContext, i)
                showNotification(time)
                i++
            }
        }
    }

    private fun showNotification(timeString: String) {
        notificationBuilder
            .setContentTitle(getString(R.string.app_name))
            .setContentText(timeString)
            .setSmallIcon(R.drawable.ic_baseline_surround_sound_24)

        notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
    }

    private fun createNotification() {
        notificationManager = baseContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, RecorderFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_SERVICE_ID)

        val channel = NotificationChannel(
            NOTIFICATION_SERVICE_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private var RECORD_SERVICE = MutableLiveData<RecordServiceEvent>()
        val recordService: LiveData<RecordServiceEvent> = RECORD_SERVICE
        const val NOTIFICATION_SERVICE_ID = "service-id"
        const val NOTIFICATION_NAME = "recording-service"
        const val NOTIFY_ID = 10
        const val START_SERVICE = "start-service"
        const val STOP_SERVICE = "stop-service"
    }
}