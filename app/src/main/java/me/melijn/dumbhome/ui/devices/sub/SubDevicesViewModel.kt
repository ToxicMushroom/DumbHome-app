package me.melijn.dumbhome.ui.devices.sub

import androidx.lifecycle.ViewModel
import me.melijn.dumbhome.ui.home.sub.DHItem

class SubDevicesViewModel : ViewModel() {
    var switchItemList: List<DHItem.SwitchItem> = ArrayList()
}