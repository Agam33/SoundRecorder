package com.ra.soundrecorder.di

import android.content.Context
import com.ra.soundrecorder.data.ISoundRecordRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class]
)
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun provideRepository(): ISoundRecordRepository
}