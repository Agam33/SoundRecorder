package com.ra.soundrecorder.utils

import com.ra.soundrecorder.data.local.entity.SoundRecordEntity
import com.ra.soundrecorder.model.SoundRecord

object DataMapper {

    fun entityToModel(soundRecordEntity: SoundRecordEntity) =
        SoundRecord(
            soundRecordEntity.id,
            soundRecordEntity.name,
            soundRecordEntity.duration,
            soundRecordEntity.filePath,
            soundRecordEntity.createdAt
    )

    fun modelToEntity(soundRecord: SoundRecord) =
        SoundRecordEntity(
            id = soundRecord.id,
            name = soundRecord.name ?: "",
            duration = soundRecord.duration,
            filePath = soundRecord.filePath ?: "",
            createdAt = soundRecord.createdAt
        )
}