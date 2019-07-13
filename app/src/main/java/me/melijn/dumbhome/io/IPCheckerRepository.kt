package me.melijn.dumbhome.io

import kotlinx.coroutines.*
import okhttp3.Request

class IPCheckerRepository {
    companion object {
        var publicIp: String = ""
    }

    fun updatePublicIp() {
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url(
                        "https://checkip.amazonaws.com"
                    )
                    .build()

                val response = SyncDeviceRepository.client.newCall(request).execute()
                publicIp = response.body?.string() ?: ""
            }
        }
    }
}