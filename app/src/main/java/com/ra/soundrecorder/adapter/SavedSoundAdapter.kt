package com.ra.soundrecorder.adapter

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ra.soundrecorder.R
import com.ra.soundrecorder.databinding.ItemSavedSoundBinding
import com.ra.soundrecorder.model.SoundRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SavedSoundAdapter(
    private val itemList: List<SoundRecord> = ArrayList(),
    private val onItemClick: (SoundRecord) -> Unit = {},
    private val onItemLongClick: (SoundRecord) -> Unit = {},
    private val onPlay: (SoundRecord) -> Unit = {},
    private val onStop: (SoundRecord) -> Unit = {},
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
            progressBar.max = (soundRecord.duration / 1000).toInt()
            btnPlayOrStop.setOnClickListener {
                isPlaying = if(!isPlaying) {
                    onPlay(soundRecord)
                    btnPlayOrStop.setImageResource(changeImageBtn(true))
                    coroutineScope.launch {
                        runProgressBar(soundRecord.duration)
                        btnPlayOrStop.setImageResource(changeImageBtn(false))
                        isPlaying = false
                        progressBar.progress = 0
                        tvDuration.text = root.context.getString(R.string.time_format_mm_ss, minutes, seconds)
                    }
                    true
                } else {
                    btnPlayOrStop.setImageResource(changeImageBtn(false))
                    onStop(soundRecord)
                    false
                }
            }
            root.setOnClickListener { onItemClick(soundRecord) }
            root.setOnLongClickListener { view ->
                onItemLongClick(soundRecord)
                true
            }
        }

        private fun changeImageBtn(play: Boolean): Int =
            if(!play) R.drawable.ic_baseline_play_circle_outline_24
            else R.drawable.ic_baseline_stop_24

        private suspend fun runProgressBar(millis: Long) = with(binding) {
            var duration = (millis / 1000).toInt()
            var i = 1
            while (isPlaying && duration >= 0) {
                progressBar.progress = i++
                tvDuration.text = root.context.getString(
                    R.string.time_format_mm_ss,
                    TimeUnit.SECONDS.toMinutes(duration.toLong()),
                    duration % 60
                )
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