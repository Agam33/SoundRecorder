package com.ra.soundrecorder.ui.recorder

import androidx.lifecycle.*
import com.ra.soundrecorder.data.SoundRecordRepository
import com.ra.soundrecorder.model.SoundRecord
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecorderViewModel @Inject constructor(
    private val soundRecordRepository: SoundRecordRepository
): ViewModel() {

    fun insertSoundRecord(soundRecord: SoundRecord) {
        viewModelScope.launch {
            soundRecordRepository.insertRecord(soundRecord)
        }
    }

    private val getRecordSize: LiveData<Int> = soundRecordRepository
        .getAllRecord()
        .asLiveData()
        .map {
            it.size
        }
}