package me.melijn.dumbhome.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.components.toSwitchComponent
import me.melijn.dumbhome.io.StateRepository
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class Database {

    companion object {
        val switches = MutableLiveData<ArrayList<SwitchComponent>>()
    }

    fun initLiveData(context: Context) {
        val json = getAsJSONObject(context)
        val jsonSwitches: JSONArray = try {
            json.getJSONArray("switches")
        } catch (ex: JSONException) {
            json.put("switches", JSONArray())
            writeDevices(context, json)
            return
        }

        val switchList = ArrayList<SwitchComponent>()
        for (i in 0 until jsonSwitches.length()) {
            val jsonSwitch = jsonSwitches.getJSONObject(i)
            switchList.add(jsonSwitch.toSwitchComponent())
        }
        switches.value = switchList
    }

    fun writeDevices(context: Context, obj: JSONObject) {
        val fos: FileOutputStream = context.openFileOutput("devices.json", Context.MODE_PRIVATE)
        fos.write(obj.toString(4).toByteArray(StandardCharsets.UTF_8))
        fos.close()
    }

    fun getAsJSONObject(context: Context) = JSONObject(readDevicesAsset(context))


    private fun readDevicesAsset(context: Context): String? {
        return try {
            val fis: FileInputStream = context.openFileInput("devices.json")
            val size: Int = fis.available()
            val buffer = ByteArray(size)
            fis.read(buffer)
            fis.close()

            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()

            null
        }
    }

    fun refreshSwitchStates(preferenceMap: Map<String, Any?>, context: Context) {
        switches.value?.let {
            val list = ArrayList<SwitchComponent>()
            it.iterator().forEach { el -> list.add(el) }

            StateRepository(preferenceMap, context).updateSwitchStates(
                list
            )
        }
    }
}