package me.melijn.dumbhome.objects

import me.melijn.dumbhome.sync.DHSyncItem
import me.melijn.dumbhome.ui.home.HomeItem
import me.melijn.dumbhome.ui.home.sub.DHItem

class ItemClickListener(
    val syncClickListener: (switchItem: DHSyncItem.SwitchItem) -> Unit = {},
    val submitClickListener: () -> Unit = {},
    val locationClickListener: (locationItem: HomeItem.LocationItem) -> Unit = {},
    val subHomeClickListener: (switchItem: DHItem.SwitchItem) -> Unit = {}
) {

    fun onClick(syncSwitchItem: DHSyncItem.SwitchItem) = syncClickListener(syncSwitchItem)
    fun onClick(switchItem: DHItem.SwitchItem) = subHomeClickListener(switchItem)
    fun onClick(locationItem: HomeItem.LocationItem) = locationClickListener(locationItem)
    fun onSubmitClick() = submitClickListener()

}