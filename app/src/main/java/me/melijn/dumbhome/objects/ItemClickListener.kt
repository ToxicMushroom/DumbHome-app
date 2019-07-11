package me.melijn.dumbhome.objects

import me.melijn.dumbhome.sync.DHItem
import me.melijn.dumbhome.ui.home.HomeItem

class ItemClickListener(
    val switchClickListener: (switchItem: DHItem.SwitchItem) -> Unit = {},
    val submitClickListener: () -> Unit = {},
    val locationClickListener: (locationItem: HomeItem.LocationItem) -> Unit = {}
) {

    fun onClick(switchItem: DHItem.SwitchItem) = switchClickListener(switchItem)
    fun onClick(locationItem: HomeItem.LocationItem) = locationClickListener(locationItem)
    fun onSubmitClick() = submitClickListener()

}