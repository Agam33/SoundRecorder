package com.ra.soundrecorder.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.model.SoundRecord
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: SoundRecordRepository
): ViewModel() {

    fun updateSoundRecord(soundRecord: SoundRecord) {
        viewModelScope.launch {
            repository.updateRecord(soundRecord)
        }
    }
}