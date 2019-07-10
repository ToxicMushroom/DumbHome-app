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

    private val _clicked = MutableLiveData<Boolean>()
    val clicked: LiveData<Boolean>
        get() = _clicked

    val switchItems = ArrayList<DHItem.SwitchItem>()

    fun onSubmitClicked() {
        _clicked.value = !(clicked.value ?: false)
    }

    fun setError(message: String) {
        _error.value = message
    }

    fun setResponse(message: String) {
        _jsonDevices.value = message
    }
}