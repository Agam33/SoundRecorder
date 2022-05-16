package com.ra.soundrecorder.data.local.room

import androidx.room.Database
import com.ra.soundrecorder.data.local.entity.SoundRecordEntity

@Database(
    entities = [SoundRecordEntity::class],
    exportSchema = true,
    version = 1
)
abstract class SoundRecorderDatabase {
    abstract fun soundRecordDao(): SoundRecordDao

}