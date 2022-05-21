package com.ra.soundrecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ra.soundrecorder.R
import com.ra.soundrecorder.databinding.ItemSavedSoundBinding
import com.ra.soundrecorder.model.SoundRecord
import com.ra.soundrecorder.utils.getTimeStringFormat
import java.util.concurrent.TimeUnit

class SavedSoundAdapter(
    private val itemList: List<SoundRecord> = ArrayList(),
    private val onItemClick: (SoundRecord) -> Unit = {}
): RecyclerView.Adapter<SavedSoundAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding: ItemSavedSoundBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(soundRecord: SoundRecord) = with(binding) {
            val minutes = (soundRecord.duration / 1000) / 60
            val seconds = (soundRecord.duration / 1000) % 60

            tvName.text = soundRecord.name
            tvDuration.text = root.context.getString(R.string.time_format_mm_ss, minutes, seconds)
            root.setOnClickListener { onItemClick(soundRecord) }
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