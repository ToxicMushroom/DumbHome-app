package me.melijn.dumbhome.io

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.sync.SyncViewModel
import okhttp3.*
import java.io.IOException

class DeviceRepository(private val map: Map<String, *>, val model: SyncViewModel) {

    companion object {
        val switches = MutableLiveData<List<SwitchComponent>>()
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
                    model.error.value = "$networkType port not set"
                    return@withContext
                } as Boolean) "https" else "http"

            val port: Int = Integer.parseInt(map.getOrElse(networkType + "_port") {
                model.error.value = "$networkType port not set"
                return@withContext
            } as String)

            val host: String = if (networkType == "local") {
                map.getOrElse("local_ip") {
                    model.error.value = "Local ip not set"
                    return@withContext
                } as String
            } else {
                map.getOrElse("remote_hostname") {
                    model.error.value = "Remote hostname not set"
                    return@withContext
                } as String
            }

            val token: String = map.getOrElse("token") {
                model.error.value = "Token not set"
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
                    CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                        withContext(Dispatchers.Main) {
                            model.error.value = "Request failed: ${e.message}"
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {

                    CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                        withContext(Dispatchers.Main) {
                            response.body?.string()?.let { convertResponseToComponents(it) }
                        }
                    }

                }
            })
        }
    }

    private fun convertResponseToComponents(response: String) {
        //TODO write actual code
        model.jsonDevices.value = response
    }
}