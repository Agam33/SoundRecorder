package com.ra.soundrecorder.di

import android.content.Context
import com.ra.soundrecorder.data.ISoundRecordRepository
import com.ra.soundrecorder.di.module.RepositoryModule
import com.ra.soundrecorder.di.module.ViewModelModule
import com.ra.soundrecorder.service.RecordingService
import com.ra.soundrecorder.ui.detail.DetailRecordActivity
import com.ra.soundrecorder.ui.recorder.RecorderFragment
import com.ra.soundrecorder.ui.saved.SavedSoundFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
interface MainComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MainComponent
    }

    fun provideRepository(): ISoundRecordRepository

    fun inject(recorderFragment: RecorderFragment)
    fun inject(savedSoundFragment: SavedSoundFragment)
    fun inject(recordingService: RecordingService)
    fun inject(detailRecordActivity: DetailRecordActivity)
}