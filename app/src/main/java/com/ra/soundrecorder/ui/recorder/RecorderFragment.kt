package com.ra.soundrecorder.ui.recorder

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.databinding.FragmentRecorderBinding
import com.ra.soundrecorder.service.RecordingService
import com.ra.soundrecorder.service.RecordingService.Companion.START_SERVICE
import com.ra.soundrecorder.service.RecordingService.Companion.STOP_SERVICE
import com.ra.soundrecorder.utils.RecordServiceEvent
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.S)
class RecorderFragment : Fragment() {

    private var _binding: FragmentRecorderBinding? = null
    private val binding get() = _binding

    private var isServiceRunning = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecorderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        actionView()
    }

    private fun observer() {
        RecordingService.recordServiceEvent.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun actionView() {
        binding?.btnPlayOrStop?.setOnClickListener {
            if(!isServiceRunning) {
                actionService(START_SERVICE)
                Timber.d("Play")
            } else {
                actionService(STOP_SERVICE)
                Timber.d("Stop")
            }
        }
    }

    private fun updateUI(event: RecordServiceEvent) {
        when(event) {
            is RecordServiceEvent.PLAY -> {
                isServiceRunning = true
                binding?.btnPlayOrStop?.text = getString(R.string.txt_play)
                binding?.tvCurrentTimeRecord?.base = SystemClock.elapsedRealtime()
                binding?.tvCurrentTimeRecord?.start()
            }
            is RecordServiceEvent.STOP -> {
                isServiceRunning = false
                binding?.btnPlayOrStop?.text = getString(R.string.txt_stop)
                binding?.tvCurrentTimeRecord?.base = SystemClock.elapsedRealtime()
                binding?.tvCurrentTimeRecord?.stop()
            }
        }
    }

    private fun actionService(action: String) {
        activity?.startService(
            Intent(context, RecordingService::class.java).apply {
                this.action = action
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}