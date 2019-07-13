package me.melijn.dumbhome.io

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.database.Database
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class StateRepository(private val preferenceMap: Map<String, *>, val context: Context) {

    fun sendSwitchUpdate(switchComponent: SwitchComponent) {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val path1 = "switches/"
                val path2 = "/state"
                val networkType: String = if ((preferenceMap.getOrElse("remote_ip") {
                        postError("remote ip not set")

                        return@withContext
                    } as String) == IPCheckerRepository.publicIp) "local" else "remote"


                val protocol: String = if (preferenceMap.getOrElse(networkType + "_https") {
                        postError("$networkType https not set")

                        return@withContext
                    } as Boolean) "https" else "http"

                val port: Int = Integer.parseInt(preferenceMap.getOrElse(networkType + "_port") {
                    postError("$networkType port not set")

                    return@withContext
                } as String)

                val host: String = if (networkType == "local") {
                    preferenceMap.getOrElse("local_ip") {
                        postError("Local ip not set")

                        return@withContext
                    } as String
                } else {
                    preferenceMap.getOrElse("remote_hostname") {
                        postError("Remote hostname not set")

                        return@withContext
                    } as String
                }
                val token: String = preferenceMap.getOrElse("token") {
                    postError("Token not set")
                    return@withContext
                } as String
                val request = Request.Builder()
                    .header("token", token)
                    .url(
                        "$protocol://$host:$port/$path1${switchComponent.id}$path2"
                    )
                    .build()

                SyncDeviceRepository.client.newCall(request = request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        postError("Request failed: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response.body?.string())
                    }
                })
            }
        }
    }

    fun updateSwitchStates(switches: ArrayList<SwitchComponent>) {

        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val path = "switches/states"
                val networkType: String = if ((preferenceMap.getOrElse("remote_ip") {
                        postError("remote ip not set")

                        return@withContext
                    } as String) == IPCheckerRepository.publicIp) "local" else "remote"


                val protocol: String = if (preferenceMap.getOrElse(networkType + "_https") {
                        postError("$networkType https not set")

                        return@withContext
                    } as Boolean) "https" else "http"

                val port: Int = Integer.parseInt(preferenceMap.getOrElse(networkType + "_port") {
                    postError("$networkType port not set")

                    return@withContext
                } as String)

                val host: String = if (networkType == "local") {
                    preferenceMap.getOrElse("local_ip") {
                        postError("Local ip not set")

                        return@withContext
                    } as String
                } else {
                    preferenceMap.getOrElse("remote_hostname") {
                        postError("Remote hostname not set")

                        return@withContext
                    } as String
                }
                val token: String = preferenceMap.getOrElse("token") {
                    postError("Token not set")
                    return@withContext
                } as String
                val request = Request.Builder()
                    .header("token", token)
                    .url(
                        "$protocol://$host:$port/$path"
                    )
                    .build()

                SyncDeviceRepository.client.newCall(request = request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        postError("Request failed: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
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
                            withContext(Dispatchers.Main) {
                                Database.switches.value = switches
                            }
                        }
                    }
                })
            }
        }
    }

    fun postError(message: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}