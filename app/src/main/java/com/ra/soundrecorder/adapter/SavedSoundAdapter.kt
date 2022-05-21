package com.ra.soundrecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ra.soundrecorder.R
import com.ra.soundrecorder.databinding.ItemSavedSoundBinding
import com.ra.soundrecorder.model.SoundRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedSoundAdapter(
    private val itemList: List<SoundRecord> = ArrayList(),
    private val onItemClickCallback: (SoundRecord) -> Unit = {},
    private val onPlay: (SoundRecord) -> Unit = {},
    private val onStop: () -> Unit = {},
    private val coroutineScope: CoroutineScope = CoroutineScope(Main)
): RecyclerView.Adapter<SavedSoundAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding: ItemSavedSoundBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private var isPlaying: Boolean = false

        fun bind(soundRecord: SoundRecord) = with(binding) {
            val minutes = (soundRecord.duration / 1000) / 60
            val seconds = (soundRecord.duration / 1000) % 60
            tvName.text = soundRecord.name
            tvDuration.text = root.context.getString(R.string.time_format_mm_ss, minutes, seconds)
            progressBar.progress = 0
            progressBar.max = (soundRecord.duration / 1000).toInt()
            btnPlayOrStop.setOnClickListener {
                isPlaying = if(!isPlaying) {
                    onPlay(soundRecord)
                    btnPlayOrStop.setImageResource(changeImageBtn(false))
                    coroutineScope.launch {
                        runProgressBar(soundRecord.duration)
                        btnPlayOrStop.setImageResource(changeImageBtn(true))
                        isPlaying = false
                        progressBar.progress = 0
                        tvDuration.text = root.context.getString(R.string.time_format_mm_ss, minutes, seconds)
                    }
                    true
                } else {
                    btnPlayOrStop.setImageResource(changeImageBtn(true))
                    onStop()
                    false
                }
            }

            root.setOnClickListener { onItemClickCallback(soundRecord) }
        }
        
        private fun changeImageBtn(state: Boolean): Int =
            if(state) R.drawable.ic_baseline_play_circle_outline_24
            else R.drawable.ic_baseline_stop_24

        private suspend fun runProgressBar(millis: Long) = with(binding) {
            var duration = (millis / 1000).toInt()
            var i = 1
            while (isPlaying && duration > 0) {
                progressBar.progress = i++
                tvDuration.text = root.context
                    .getString(R.string.time_format_mm_ss, 0, duration)
                delay(1000)
                duration--
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemSavedSoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}