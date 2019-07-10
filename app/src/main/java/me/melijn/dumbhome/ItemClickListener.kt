package me.melijn.dumbhome

import me.melijn.dumbhome.components.SwitchComponent

class ItemClickListener(val clickListener: (switchComponent: SwitchComponent) -> Unit) {
    fun onClick(switchComponent: SwitchComponent) = clickListener(switchComponent)
}