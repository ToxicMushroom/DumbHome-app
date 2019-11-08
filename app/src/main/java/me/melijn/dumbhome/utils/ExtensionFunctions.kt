package me.melijn.dumbhome.utils

import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ExtensionFunctions {
    fun init(context: Context) {
        displayDensity = context.resources.displayMetrics.density
    }

    companion object {
        var displayDensity = 1.0f
    }
}

fun Int.toPx(): Int = (this * ExtensionFunctions.displayDensity).toInt()

fun Int.toDp(): Int = (this / ExtensionFunctions.displayDensity).toInt()


suspend fun Call.await(failureHandler: ((call: Call, e: Exception) -> Unit)? = null): Response {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                // Don't bother with resuming the continuation if it is already cancelled.
                if (continuation.isCancelled) return
                if (failureHandler != null) failureHandler.invoke(call, e)
                else continuation.resumeWithException(e)
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                //Ignore cancel exception
            }
        }
    }
}

fun String.toUpperWordCase(): String {
    var previous = ' '
    var newString = ""
    this.toCharArray().forEach { c: Char ->
        newString += if (previous == ' ') c.toUpperCase() else c.toLowerCase()
        previous = c
    }
    return newString
}