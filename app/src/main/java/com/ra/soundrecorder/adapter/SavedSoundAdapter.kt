package com.ra.soundrecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ra.soundrecorder.databinding.ItemSavedSoundBinding
import com.ra.soundrecorder.model.SoundRecord
import com.ra.soundrecorder.utils.getTimeStringFormat

class SavedSoundAdapter(
    private val itemList: List<SoundRecord> = ArrayList(),
    private val onItemClick: (SoundRecord) -> Unit = {}
): RecyclerView.Adapter<SavedSoundAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding: ItemSavedSoundBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(soundRecord: SoundRecord) = with(binding) {
            tvName.text = soundRecord.name
            tvDuration.text = getTimeStringFormat(root.context, soundRecord.duration.toInt())
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