package me.melijn.dumbhome.components

import me.melijn.dumbhome.utils.toUpperWordCase

enum class Location {
    FABIAN_KAMER, MERLIJN_KAMER, SPEELKAMER, WOONKAMER, BUITEN, ANDERE;

    fun toName(): String = this.toString()
        .replace("_", " ")
        .toUpperWordCase()
}


fun String.toLocation(): Location {
    return when (this) {
        Location.FABIAN_KAMER.toString(), Location.FABIAN_KAMER.toName() -> Location.FABIAN_KAMER
        Location.MERLIJN_KAMER.toString(), Location.MERLIJN_KAMER.toName() -> Location.MERLIJN_KAMER
        Location.SPEELKAMER.toString(), Location.SPEELKAMER.toName() -> Location.SPEELKAMER
        Location.WOONKAMER.toString(), Location.WOONKAMER.toName() -> Location.WOONKAMER
        Location.BUITEN.toString(), Location.BUITEN.toName() -> Location.BUITEN
        else -> Location.ANDERE
    }
}