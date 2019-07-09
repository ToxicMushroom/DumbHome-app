package me.melijn.dumbhome.sync

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.ActivitySyncBinding
import me.melijn.dumbhome.io.DeviceRepository

class SyncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySyncBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_sync
        )

        supportActionBar?.setTitle(R.string.title_activity_sync)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this

        val model: SyncViewModel = ViewModelProviders.of(this).get(SyncViewModel::class.java)
        binding.synViewModel = model

        val adapter = SyncDevicesAdapter()
        DeviceRepository(
            PreferenceManager.getDefaultSharedPreferences(applicationContext).all,
            model
        )

        model.jsonDevices.observe(this, Observer {
            binding.textViewOne.text = it
        })

        DeviceRepository.switches.observe(this, Observer {

            //TODO convert switchcomponent list to switchItem list then submit
            //adapter.submitList()
        })

        binding.deviceSyncList.adapter = adapter


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
