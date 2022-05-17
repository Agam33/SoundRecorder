package com.ra.soundrecorder.ui.saved

import androidx.lifecycle.ViewModel
import com.ra.soundrecorder.data.SoundRecordRepository
import javax.inject.Inject

class SavedSoundViewModel @Inject constructor(
    soundRecordRepository: SoundRecordRepository
): ViewModel() {
}