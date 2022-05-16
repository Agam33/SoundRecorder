package com.ra.soundrecorder.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SoundRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val duration: String,
)