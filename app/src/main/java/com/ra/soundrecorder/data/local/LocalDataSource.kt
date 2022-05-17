package com.ra.soundrecorder.data.local

import com.ra.soundrecorder.data.local.room.SoundRecordDao
import javax.inject.Inject
import javax.inject.Singleton


interface ILocalDataSource {

}

@Singleton
class LocalDataSource @Inject constructor(
    private val soundRecordDao: SoundRecordDao
): ILocalDataSource {



}