package me.melijn.dumbhome.objects

import me.melijn.dumbhome.sync.DHSyncItem
import me.melijn.dumbhome.ui.home.HomeItem

class ItemClickListener(
    val switchClickListener: (switchItem: DHSyncItem.SwitchItem) -> Unit = {},
    val submitClickListener: () -> Unit = {},
    val locationClickListener: (locationItem: HomeItem.LocationItem) -> Unit = {}
) {

    fun onClick(switchItem: DHSyncItem.SwitchItem) = switchClickListener(switchItem)
    fun onClick(locationItem: HomeItem.LocationItem) = locationClickListener(locationItem)
    fun onSubmitClick() = submitClickListener()

}