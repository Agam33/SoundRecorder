package com.ra.soundrecorder.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.model.SoundRecord
import javax.inject.Inject

class SavedSoundViewModel @Inject constructor(
    private val soundRecordRepository: SoundRecordRepository
): ViewModel() {

    fun getAllRecord(): LiveData<List<SoundRecord>> =
        soundRecordRepository.getAllRecord().asLiveData()
}