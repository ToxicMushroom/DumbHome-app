package me.melijn.dumbhome

import me.melijn.dumbhome.sync.DHItem

class ItemClickListener(val clickListener: (switchItem: DHItem.SwitchItem) -> Unit) {
    fun onClick(switchItem: DHItem.SwitchItem) = clickListener(switchItem)
}