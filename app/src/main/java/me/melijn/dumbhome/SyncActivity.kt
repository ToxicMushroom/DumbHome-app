package me.melijn.dumbhome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SyncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        supportActionBar?.setTitle(R.string.title_activity_sync)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
