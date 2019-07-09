package me.melijn.dumbhome.sync

import androidx.lifecycle.ViewModel
import me.melijn.dumbhome.database.Database

class SyncViewModel : ViewModel() {

    val switches = Database.switches

}