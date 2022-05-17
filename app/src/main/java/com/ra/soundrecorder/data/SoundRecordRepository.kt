package com.ra.soundrecorder.data

import com.ra.soundrecorder.data.local.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface ISoundRecordRepository {

}

@Singleton
class SoundRecordRepository @Inject constructor(
    private val localDataSource: LocalDataSource
): ISoundRecordRepository {

}
