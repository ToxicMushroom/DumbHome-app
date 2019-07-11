package me.melijn.dumbhome.utils

import android.content.Context

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