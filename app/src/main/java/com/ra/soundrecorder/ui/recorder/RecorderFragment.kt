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
import com.ra.soundrecorder.service.RecordingService.Companion.currentTimeService
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
        binding?.btnMic?.setOnClickListener {
            if(!isServiceRunning) {
                actionService(START_SERVICE)
                Timber.d(getString(R.string.txt_play))
            } else {
                actionService(STOP_SERVICE)
                Timber.d(getString(R.string.txt_stop))
            }
        }
    }

    private fun updateUI(event: RecordServiceEvent) {
        when(event) {
            is RecordServiceEvent.PLAY -> {
                isServiceRunning = true
                binding?.btnMic?.playAnimation()
                currentTimeService.observe(viewLifecycleOwner, ::servicePlay)
            }
            is RecordServiceEvent.STOP -> serviceStop()
        }
    }

    private fun servicePlay(time: String) {
        binding?.tvCurrentTimeRecord?.text = time
    }

    private fun serviceStop() {
        isServiceRunning = false
        binding?.btnMic?.cancelAnimation()
        binding?.tvCurrentTimeRecord?.text = getString(R.string.default_record_time)
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