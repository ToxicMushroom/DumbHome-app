package me.melijn.dumbhome.components

//TODO convert to kotlin
class SwitchComponent(name: String, location: Location, val id: Int, var isOn: Boolean) :
    Component(name, location, ComponentType.SWITCH)
