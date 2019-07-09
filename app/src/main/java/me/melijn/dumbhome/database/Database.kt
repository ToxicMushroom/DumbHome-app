package me.melijn.dumbhome.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import me.melijn.dumbhome.components.Location
import me.melijn.dumbhome.components.SwitchComponent
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class Database {

    companion object {
        val switches = MutableLiveData<List<SwitchComponent>>()
    }

    fun initLiveData(context: Context) {
        val json = getAsJSONObject(context)
        val jsonSwitches = json.getJSONArray("switches")
        val switchList = ArrayList<SwitchComponent>()
        for (i in 0..jsonSwitches.length()) {
            val jsonSwitch = jsonSwitches.getJSONObject(i)
            val switch = SwitchComponent(
                jsonSwitch.getString("name"),
                Location.valueOf(jsonSwitch.getString("location")),
                jsonSwitch.getInt("id"),
                false
            )
            switchList.add(switch)
        }
        switches.value = switchList
    }



    fun writeDevices(context: Context, obj: JSONObject) {
        val fos: FileOutputStream = context.openFileOutput("devices.json", Context.MODE_PRIVATE)
        fos.write(obj.toString(4).toByteArray(StandardCharsets.UTF_8))
        fos.close()
    }

    fun getAsJSONObject(context: Context): JSONObject {
        return JSONObject(readDevicesAsset(context))
    }

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
}