package com.ra.soundrecorder.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.databinding.ActivityDetailRecordBinding
import com.ra.soundrecorder.model.SoundRecord
import com.ra.soundrecorder.ui.ViewModelFactory
import com.ra.soundrecorder.utils.updateFile
import javax.inject.Inject

class DetailRecordActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel : DetailViewModel by viewModels { factory }

    private val binding: ActivityDetailRecordBinding by lazy {
        ActivityDetailRecordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val soundRecord = intent.getParcelableExtra<SoundRecord>(DETAIL_EXTRA_BUNDLE) as? SoundRecord ?: SoundRecord()
        setupUI(soundRecord)
        setChange(soundRecord)
    }

    private fun setChange(soundRecord: SoundRecord) = with(binding) {
        btnUpdateFile.setOnClickListener {
            val newName = edName.text.toString()
            updateFile(newName, soundRecord, application) { newSoundRecord ->
                viewModel.updateSoundRecord(newSoundRecord)
                Toast.makeText(this@DetailRecordActivity, "File updated", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun setupUI(soundRecord: SoundRecord) = with(binding) {
        val minutes = (soundRecord.duration / 1000) / 60
        val seconds = (soundRecord.duration / 1000) % 60
        edName.hint = soundRecord.name
        tvDuration.text = getString(R.string.time_format_mm_ss, minutes, seconds)
        tvFilePath.text = soundRecord.filePath
    }

    companion object {
        const val DETAIL_EXTRA_BUNDLE = "extra-bundle"
    }
}