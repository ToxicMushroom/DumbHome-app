package me.melijn.dumbhome.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.melijn.dumbhome.database.Database

class HomeViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    fun initValues(context: Context) {
        _text.value = Database().getAsJSONObject(context).toString(4)
    }
}