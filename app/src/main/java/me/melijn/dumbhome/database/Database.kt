package me.melijn.dumbhome.database

import android.content.Context
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


class Database {

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