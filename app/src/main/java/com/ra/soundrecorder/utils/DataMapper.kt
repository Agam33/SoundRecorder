package com.ra.soundrecorder.utils

import com.ra.soundrecorder.data.local.entity.SoundRecordEntity
import com.ra.soundrecorder.model.SoundRecord

object DataMapper {
    fun entityToModel(soundRecordEntity: SoundRecordEntity) =
        SoundRecord(
            soundRecordEntity.id,
            soundRecordEntity.name,
            soundRecordEntity.duration
    )
    fun modelToEntity(soundRecord: SoundRecord) =
        SoundRecordEntity(
            soundRecord.id,
            soundRecord.name,
            soundRecord.duration
        )
}