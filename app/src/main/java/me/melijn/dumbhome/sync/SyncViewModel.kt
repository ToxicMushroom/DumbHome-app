package me.melijn.dumbhome.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SyncViewModel : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _jsonDevices = MutableLiveData<String>()
    val jsonDevices: LiveData<String>
        get() = _jsonDevices

    val switchItems = ArrayList<DHSyncItem.SwitchItem>()

    fun setError(message: String) {
        _error.value = message
    }

    fun setResponse(message: String) {
        _jsonDevices.value = message
    }
}