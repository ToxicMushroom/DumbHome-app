package me.melijn.dumbhome.io

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import me.melijn.dumbhome.components.SwitchComponent
import okhttp3.OkHttpClient
import okhttp3.Request

class DeviceRepository(val url: String) {

    companion object {
        lateinit var switches: LiveData<List<SwitchComponent>>
        val client = OkHttpClient()
    }

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            refreshDeviceRepository()
        }
    }

    suspend fun refreshDeviceRepository() {
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .build()
        }
    }
}