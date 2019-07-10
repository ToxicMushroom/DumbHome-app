package me.melijn.dumbhome.components

import android.content.Context
import me.melijn.dumbhome.database.Database
import org.json.JSONArray
import org.json.JSONObject

class SwitchComponent(name: String, location: Location, val id: Int, var isOn: Boolean) :
    Component(name, location, ComponentType.SWITCH) {

    fun toJSONString(): String {
        return "{" +
                "\n    \"name\": \"$name\"," +
                "\n    \"location\": \"$location\"," +
                "\n    \"id\": $id" +
                "}"
    }
}

fun JSONObject.toSwitchComponent(): SwitchComponent {
    return SwitchComponent(
        name = getString("name"),
        location = Location.valueOf(getString("location")),
        id = getInt("id"),
        isOn = false
    )
}
//
//fun Database.removeSwitch(context: Context, switchComponent: SwitchComponent) {
//    val json = getAsJSONObject(context)
//    val jsonSwitches = json.getJSONArray("switches")
//
//    for (i in 0..jsonSwitches.length()) {
//        if (jsonSwitches.getJSONObject(i).toSwitchComponent().id == switchComponent.id) {
//            jsonSwitches.remove(i)
//        }
//    }
//
//    Database.switches.value?.remove(switchComponent)
//    writeDevices(context, json.put("switches", jsonSwitches))
//}
//
//fun Database.addSwitch(context: Context, switchComponent: SwitchComponent) {
//    val json = getAsJSONObject(context)
//    val jsonSwitches = json.getJSONArray("switches")
//
//    for (i in 0..jsonSwitches.length()) {
//        if (jsonSwitches.getJSONObject(i).toSwitchComponent().id == switchComponent.id) return
//    }
//
//    jsonSwitches.put(switchComponent.toJSONString())
//
//    Database.switches.value?.add(switchComponent)
//    writeDevices(context, json.put("switches", jsonSwitches))
//}

fun Database.setSwitches(context: Context, switchComponents: List<SwitchComponent>) {
    val json = getAsJSONObject(context)
    val jsonSwitches = JSONArray()


    for (switchComponent in switchComponents) {
        jsonSwitches.put(JSONObject(switchComponent.toJSONString()))
    }

    Database.switches.value = ArrayList(switchComponents)
    writeDevices(context, json.put("switches", jsonSwitches))
}