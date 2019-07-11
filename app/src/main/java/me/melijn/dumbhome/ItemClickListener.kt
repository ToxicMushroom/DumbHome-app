package me.melijn.dumbhome

import me.melijn.dumbhome.sync.DHItem

class ItemClickListener(
    val switchClickListener: (switchItem: DHItem.SwitchItem) -> Unit = {},
    val submitClickListener: () -> Unit = {}
) {

    fun onClick(switchItem: DHItem.SwitchItem) = switchClickListener(switchItem)
    fun onSubmitClick() = submitClickListener()

}