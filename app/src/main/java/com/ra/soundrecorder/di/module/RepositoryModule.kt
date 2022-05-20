package com.ra.soundrecorder.di.module

import com.ra.soundrecorder.data.ISoundRecordRepository
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.di.module.DatabaseModule
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(
        soundRecordRepository: SoundRecordRepository
    ): ISoundRecordRepository
}