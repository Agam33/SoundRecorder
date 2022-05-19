package com.ra.soundrecorder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ra.soundrecorder.ui.ViewModelFactory
import com.ra.soundrecorder.ui.recorder.RecorderViewModel
import com.ra.soundrecorder.ui.saved.SavedSoundViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SavedSoundViewModel::class)
    abstract fun bindSaveSoundViewModel(viewModel: SavedSoundViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecorderViewModel::class)
    abstract fun bindRecorderViewModel(viewModel: RecorderViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}