package me.melijn.dumbhome.io

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.ui.home.sub.DHItem
import me.melijn.dumbhome.utils.await
import okhttp3.MultipartBody
import okhttp3.Request
import org.json.JSONObject

class StateRepository(private val preferenceMap: Map<String, *>, val context: Context) {

    fun sendSwitchUpdate(switchItem: DHItem.SwitchItem) =
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val path1 = "switches/"
            val path2 = "/state"
            val networkType: String = if ((preferenceMap.getOrElse("remote_ip") {
                    postError("remote ip not set")

                    return@launch
                } as String) == IPCheckerRepository.publicIp) "local" else "remote"


            val protocol: String = if (preferenceMap.getOrElse(networkType + "_https") {
                    postError("$networkType https not set")

                    return@launch
                } as Boolean) "https" else "http"

            val port: Int = Integer.parseInt(preferenceMap.getOrElse(networkType + "_port") {
                postError("$networkType port not set")

                return@launch
            } as String)

            val host: String = if (networkType == "local") {
                preferenceMap.getOrElse("local_ip") {
                    postError("Local ip not set")

                    return@launch
                } as String
            } else {
                preferenceMap.getOrElse("remote_hostname") {
                    postError("Remote hostname not set")

                    return@launch
                } as String
            }
            val token: String = preferenceMap.getOrElse("token") {
                postError("Token not set")
                return@launch
            } as String

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("state", switchItem.state.value.toString())
                .build()

            val request = Request.Builder()
                .header("token", token)
                .url(
                    "$protocol://$host:$port/$path1${switchItem.switchComponent.id}$path2"
                )
                .post(requestBody)
                .build()

            val response = SyncDeviceRepository.client.newCall(request = request).await { _, e ->
                e.printStackTrace()
                postError("Request failed: ${e.message}")
            }
            println(response.body?.string())
        }

    fun updateSwitchStates(switches: ArrayList<SwitchComponent>) =
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {

            val path = "switches/states"
            val networkType: String = if ((preferenceMap.getOrElse("remote_ip") {
                    postError("remote ip not set")

                    return@launch
                } as String) == IPCheckerRepository.publicIp) "local" else "remote"


            val protocol: String = if (preferenceMap.getOrElse(networkType + "_https") {
                    postError("$networkType https not set")

                    return@launch
                } as Boolean) "https" else "http"

            val port: Int = Integer.parseInt(preferenceMap.getOrElse(networkType + "_port") {
                postError("$networkType port not set")

                return@launch
            } as String)

            val host: String = if (networkType == "local") {
                preferenceMap.getOrElse("local_ip") {
                    postError("Local ip not set")

                    return@launch
                } as String
            } else {
                preferenceMap.getOrElse("remote_hostname") {
                    postError("Remote hostname not set")

                    return@launch
                } as String
            }
            val token: String = preferenceMap.getOrElse("token") {
                postError("Token not set")
                return@launch
            } as String
            val request = Request.Builder()
                .header("token", token)
                .url(
                    "$protocol://$host:$port/$path"
                )
                .build()

            val response = SyncDeviceRepository.client.newCall(request = request).await { _, e ->
                e.printStackTrace()
                postError("Request failed: ${e.message}")
            }

            response.body?.string()?.let { it ->
                val responseObj = JSONObject(it)
                val switchArray = responseObj.getJSONArray("switches")
                for (i in 0 until switchArray.length()) {
                    val switch = JSONObject(switchArray.getString(i))

                    val switchComponent =
                        switches.find { match -> match.id == switch.getInt("id") }
                    switches.removeAll { match -> match.id == switch.getInt("id") }
                    switchComponent?.isOn = switch.getBoolean("state")
                    switchComponent?.let { it1 -> switches.add(it1) }
                }
            }
            CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                Database.switches.value = switches
            }
        }

    private fun postError(message: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}