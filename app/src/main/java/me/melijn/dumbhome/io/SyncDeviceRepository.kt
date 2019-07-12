package me.melijn.dumbhome.io

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.components.toSwitchComponent
import me.melijn.dumbhome.sync.SyncViewModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SyncDeviceRepository(private val map: Map<String, *>, val model: SyncViewModel) {

    companion object {
        val switches = MutableLiveData<ArrayList<SwitchComponent>>()
        val client = OkHttpClient()
    }

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            refreshDeviceRepository()
        }
    }

    suspend fun refreshDeviceRepository() {
        withContext(Dispatchers.IO) {
            val path = "presets/list"
            val query = "?global=false"
            val networkType = if (false) "remote" else "local"

            val protocol: String = if (map.getOrElse(networkType + "_https") {
                    postError("$networkType https not set")

                    return@withContext
                } as Boolean) "https" else "http"

            val port: Int = Integer.parseInt(map.getOrElse(networkType + "_port") {
                postError("$networkType port not set")

                return@withContext
            } as String)

            val host: String = if (networkType == "local") {
                map.getOrElse("local_ip") {
                    postError("Local ip not set")

                    return@withContext
                } as String
            } else {
                map.getOrElse("remote_hostname") {
                    postError("Remote hostname not set")

                    return@withContext
                } as String
            }

            val token: String = map.getOrElse("token") {
                postError("Token not set")

                return@withContext
            } as String

            val request = Request.Builder()
                .header("token", token)
                .url(
                    "$protocol://$host:$port/$path$query"
                )
                .build()

            client.newCall(request = request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    postError("Request failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.string()?.let { convertResponseToComponents(it) }
                }
            })
        }
    }

    fun postError(message: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                model.setError(message)
            }
        }
    }

    private fun convertResponseToComponents(response: String) {
        val jsonPresetObj = JSONObject(response).getJSONObject("presets")
        val switchArray = jsonPresetObj.getJSONArray("switches")
        val previousSwitchList = ArrayList<SwitchComponent>()
        for (i in 0 until switchArray.length()) {
            previousSwitchList.add(switchArray.getJSONObject(i).toSwitchComponent())
        }
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                model.setResponse("")
                switches.value = previousSwitchList
            }
        }

    }
}