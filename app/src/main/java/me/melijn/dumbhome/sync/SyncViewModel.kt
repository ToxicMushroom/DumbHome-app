package me.melijn.dumbhome.sync

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SyncViewModel : ViewModel() {
    val error = MutableLiveData<String>()
    val jsonDevices = MutableLiveData<String>()
}