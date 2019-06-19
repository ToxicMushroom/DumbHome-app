package me.melijn.dumbhome.ui.devices

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager

class DevicesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is devices Fragment"
    }
    val text: LiveData<String>
        get() = _text

    fun initValues(context: Context?) {
        _text.value = PreferenceManager.getDefaultSharedPreferences(context).all.toString()
    }
}